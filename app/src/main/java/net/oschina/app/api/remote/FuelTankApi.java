package net.oschina.app.api.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import net.oschina.app.api.ApiHttpClient;
import net.oschina.app.api.FuelTankApiHttpClient;
import net.oschina.app.api.LeanCloudApiHttpClient;
import net.oschina.app.api.LeanCloudParameters;
import net.oschina.app.improve.account.AccountHelper;
import net.oschina.app.improve.bean.simple.About;
import net.oschina.app.improve.constant.TopicType;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.tencent.open.utils.Global.getContext;
import static net.oschina.app.api.ApiHttpClient.post;

/**
 * LeanCloud api v1
 */
@SuppressWarnings("all")
public class FuelTankApi {

    public static final int CATALOG_ALL = 0;
    public static final int CATALOG_SOFTWARE = 1;
    public static final int CATALOG_QUESTION = 2;
    public static final int CATALOG_BLOG = 3;
    public static final int CATALOG_TRANSLATION = 4;
    public static final int CATALOG_EVENT = 5;
    public static final int CATALOG_NEWS = 6;
    public static final int CATALOG_TWEET = 100;

    public static final int COMMENT_SOFT = 1;    // 软件推荐-不支持(默认软件评论其实是动弹)
    public static final int COMMENT_QUESTION = 2;    // 讨论区帖子
    public static final int COMMENT_BLOG = 3;    // 博客
    public static final int COMMENT_TRANSLATION = 4;    // 翻译文章
    public static final int COMMENT_EVENT = 5;    // 活动类型
    public static final int COMMENT_NEWS = 6;    // 资讯类型
    public static final int COMMENT_TWEET = 100;  // 动弹

    public static final int COMMENT_HOT_ORDER = 2; //热门评论顺序
    public static final int COMMENT_NEW_ORDER = 1; //最新评论顺序

    public static final int CATALOG_BANNER_NEWS = 1; // 资讯Banner
    public static final int CATALOG_BANNER_BLOG = 2; // 博客Banner
    public static final int CATALOG_BANNER_EVENT = 3; // 活动Banner

    public static final int CATALOG_BLOG_NORMAL = 1; // 最新
    public static final int CATALOG_BLOG_HEAT = 2; // 最热
    public static final int CATALOG_BLOG_RECOMMEND = 3; //推荐

    public static final String CATALOG_NEWS_DETAIL = "news";
    public static final String CATALOG_TRANSLATE_DETAIL = "translation";
    public static final String CATALOG_SOFTWARE_DETAIL = "software";

    public static final String LOGIN_WEIBO = "weibo";
    public static final String LOGIN_QQ = "qq";
    public static final String LOGIN_WECHAT = "wechat";
    public static final String LOGIN_CSDN = "csdn";

    public static final int REGISTER_INTENT = 1;
    public static final int RESET_PWD_INTENT = 2;

    public static final int REQUEST_COUNT = 0x50;//请求分页大小

    public static final int TYPE_USER_FLOWS = 1;//你关注的人
    public static final int TYPE_USER_FANS = 2;//关注你的人

    /**
     * 动弹列表
     *
     * @param aid     author id, 请求某人的动弹
     * @param tag     相关话题
     * @param type    1: 广场（所有动弹）， 2：朋友圈（好友动弹）
     * @param order   1: 最新， 2：最热
     * @param handler handler
     */
    public static void getTweetList(Long authorId, String tag, Integer type, Integer order
            , String pageToken, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (authorId != null) {
            params.put("authorId", authorId);
        } else if (!TextUtils.isEmpty(tag)) {
            params.put("tag", tag);
        } else {
            params.put("type", type);
        }
        params.put("order", order);
        params.put("pageToken", pageToken);
        FuelTankApiHttpClient.post("vehicle_circle/tweet/tweet_list", params, handler);
    }

    /**
     * 发布动弹
     * 链接 http://doc.oschina.net/app_v2?t=105522
     *
     * @param content     内容
     * @param imagesToken 图片token
     * @param audioToken  语音token
     * @param share       相关分享节点，仅仅关注 {@link About.Share#id}, {@link About.Share#type},
     *                    {@link About.Share#fromTweetId}
     * @param handler     回调
     */
    public static void pubTweet(String content, ArrayList<String> imagesUrls, String audioToken, About.Share share, AsyncHttpResponseHandler handler) {
        if (TextUtils.isEmpty(content))
            throw new NullPointerException("content is not null.");
        RequestParams params = new RequestParams();
        params.put("authorId", AccountHelper.getUserId());
        params.put("content", content);
        //params.put("images", imagesUrls);
        String jsonArrayStr = "[ ";

        for(int i = 0; i < imagesUrls.size(); i++)
        {
            String url = imagesUrls.get(i);
            int w = 0;
            int h = 0;
            String pureUrl = url.substring(0, url.lastIndexOf("?"));
            w = Integer.parseInt(url.substring(url.lastIndexOf("w=")+2, url.lastIndexOf("h=")-1));
            h = Integer.parseInt(url.substring(url.lastIndexOf("h=")+2));
//            try
//            {
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//
//                /**
//                 * 最关键在此，把options.inJustDecodeBounds = true;
//                 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
//                 */
//                options.inJustDecodeBounds = true;
//                Bitmap bitmap = BitmapFactory.decodeFile(url, options); // 此时返回的bitmap为null
//
//                /**
//                 *options.outHeight为原始图片的高
//                 */
//                w = options.outWidth;
//                h = options.outHeight;
////
////                URL urlObj = new URL(url);
////                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
////                conn.setDoInput(true);
////                conn.connect();
////                InputStream is = conn.getInputStream();
////                Bitmap image = BitmapFactory.decodeStream(is);
////                w = image.getWidth();      // 源图宽度
////                h = image.getHeight();    // 源图高度
////                is.close();
////                conn.disconnect();
////                Glide.with(getContext())
////                        .load(url)
////                        .asBitmap()//强制Glide返回一个Bitmap对象
////                        .into(new SimpleTarget<Bitmap>() {
////                            @Override
////                            public final void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
////                               final int w1= bitmap.getWidth();
////                                final int h1 = bitmap.getHeight();
////
////                            }
////                        });
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }

            String fileName = "";
            if (url != null)
            {
                fileName = url.substring(url.lastIndexOf('/')+1);
            }

            String href = "";
            String thumb = url;
            String type = "0";

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("h", h);
            jsonObj.put("w", w);
            jsonObj.put("href", url);
            jsonObj.put("name", fileName);
            jsonObj.put("type", type);
            jsonObj.put("thumb", url);
            String jsonStr = jsonObj.toString();
            jsonArrayStr += jsonStr;
            jsonArrayStr += ", ";
        }
        jsonArrayStr += " ]";
        params.put("images", jsonArrayStr);
//        params.put("audio", audioToken);
//        if (About.check(share)) {
//            params.put("aboutId", share.id);
//            params.put("aboutType", share.type);
//            params.put("aboutFromTweetId", share.fromTweetId);
//        }
        FuelTankApiHttpClient.post("vehicle_circle/tweet/publish", params, handler);
    }

    /**
     * 请求动弹评论列表
     *
     * @param sourceId 动弹id
     * @param handler  回调
     */
    public static void getTweetCommentList(long sourceId, String pageToken, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("sourceId", sourceId);
        params.put("pageToken", pageToken);
        FuelTankApiHttpClient.post("vehicle_circle/tweet/comment_list", params, handler);
    }


    /**
     * 更改动弹点赞状态
     *
     * @param sourceId 动弹id
     * @param handler  回调
     */
    public static void reverseTweetLike(long sourceId, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("topicId", sourceId);
        params.put("topicType", TopicType.POST);
        params.put("uid", AccountHelper.getUserId());
        FuelTankApiHttpClient.post("vehicle_circle/tweet/like", params, handler);
    }
}
