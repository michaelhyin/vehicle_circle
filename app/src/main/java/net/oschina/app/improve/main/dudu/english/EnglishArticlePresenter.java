package net.oschina.app.improve.main.dudu.english;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import net.oschina.app.OSCApplication;
import net.oschina.app.R;
import net.oschina.app.api.LeanCloudPatternOpt;
import net.oschina.app.api.remote.LeanCloudApi;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.improve.app.AppOperator;
import net.oschina.app.improve.bean.Article;
import net.oschina.app.improve.bean.Launcher;
import net.oschina.app.improve.bean.base.PageBean;
import net.oschina.app.improve.bean.base.ResultBean;
import net.oschina.app.improve.main.update.OSCSharedPreference;
import net.oschina.app.improve.utils.CacheManager;
import net.oschina.common.utils.CollectionUtil;
import net.oschina.common.utils.StreamUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.Header;

/**
 * 英文推荐界面
 * Created by huanghaibin on 2017/10/23.
 */

class EnglishArticlePresenter implements EnglishArticleContract.Presenter {
    private final EnglishArticleContract.View mView;
    private String mNextToken;
    private static final String CACHE_NAME = "english_article_list";
    private static final int TYPE_ENGLISH = 8000;//获取英文

    EnglishArticlePresenter(EnglishArticleContract.View nView) {
        this.mView = nView;
        this.mView.setPresenter(this);
        getLaunch();
    }

    @Override
    public void loadCache() {
        List<Article> items = CacheManager.readListJson(OSCApplication.getInstance(), CACHE_NAME, Article.class);
        if (items != null) {
            mView.onRefreshSuccess(items);
            mView.onComplete();
        }
    }

    @Override
    public void onRefreshing() {
        LeanCloudApi.getArticles(
                OSCSharedPreference.getInstance().getDeviceUUID(),
                TYPE_ENGLISH,
                "",
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {
                            mView.showNotMore();
                            mView.onComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.onComplete();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            responseString = LeanCloudPatternOpt.getArticlesPatternStr(responseString);
                            Type type = new TypeToken<ResultBean<PageBean<Article>>>() {
                            }.getType();
                            ResultBean<PageBean<Article>> bean = new Gson().fromJson(responseString, type);
                            if (bean != null && bean.isSuccess()) {
                                PageBean<Article> pageBean = bean.getResult();
                                List<Article> list = pageBean.getItems();
                                pageBean.setNextPageToken(list.size());
                                mNextToken = pageBean.getNextPageToken();
                                for (Article article : list) {
                                    article.setImgs(removeImgs(article.getImgs()));
                                }
                                CacheManager.saveToJson(OSCApplication.getInstance(), CACHE_NAME, list);
                                mView.onRefreshSuccess(list);
                                if (list.size() == 0) {
                                    mView.showNotMore();
                                }
                            } else {
                                mView.showNotMore();
                            }
                            mView.onComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.showNotMore();
                            mView.onComplete();
                        }
                    }
                });
    }

    @Override
    public void onLoadMore() {
        LeanCloudApi.getArticles(
                OSCSharedPreference.getInstance().getDeviceUUID(),
                TYPE_ENGLISH,
                mNextToken,
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mView.showNetworkError(R.string.state_network_error);
                        mView.onComplete();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            responseString = LeanCloudPatternOpt.getArticlesPatternStr(responseString);
                            Type type = new TypeToken<ResultBean<PageBean<Article>>>() {
                            }.getType();
                            ResultBean<PageBean<Article>> bean = new Gson().fromJson(responseString, type);
                            if (bean != null && bean.isSuccess()) {
                                PageBean<Article> pageBean = bean.getResult();
                                List<Article> list = pageBean.getItems();
                                pageBean.setNextPageToken(list.size());
                                mNextToken = pageBean.getNextPageToken();
                                for (Article article : list) {
                                    article.setImgs(removeImgs(article.getImgs()));
                                }
                                mView.onLoadMoreSuccess(list);
                                if (list.size() == 0) {
                                    mView.showNotMore();
                                }
                            } else {
                                mView.showNotMore();
                            }
                            mView.onComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.showNotMore();
                            mView.onComplete();
                        }
                    }
                });
    }

    private static String[] removeImgs(String[] imgs) {
        if (imgs == null || imgs.length == 0)
            return null;
        List<String> list = new ArrayList<>();
        for (String img : imgs) {
            if (!TextUtils.isEmpty(img)) {
                if (img.startsWith("http")) {
                    list.add(img);
                }
            }
        }
        return CollectionUtil.toArray(list, String.class);
    }

    private static void getLaunch() {
        OSChinaApi.getLauncher(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Type type = new TypeToken<ResultBean<Launcher>>() {
                    }.getType();
                    ResultBean<Launcher> bean = new Gson().fromJson(responseString, type);
                    if (bean != null && bean.isSuccess() && bean.getResult() != null) {
                        CacheManager.saveToJson(OSCApplication.getInstance(), "Launcher.json", bean.getResult());
                        saveAdImage(bean.getResult());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void saveAdImage(Launcher launcher) {
        final Future<File> future = Glide.with(OSCApplication.getInstance())
                .load(launcher.getImgUrl())
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                try {
                    File sourceFile = future.get();
                    if (sourceFile == null || !sourceFile.exists())
                        return;
                    String savePath = OSCApplication.getInstance().getCacheDir() + "/launcher";
                    final File saveFile = new File(savePath);
                    StreamUtil.copyFile(sourceFile, saveFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
