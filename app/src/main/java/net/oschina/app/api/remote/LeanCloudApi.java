package net.oschina.app.api.remote;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import net.oschina.app.api.ApiHttpClient;
import net.oschina.app.api.LeanCloudApiHttpClient;
import net.oschina.app.api.LeanCloudParameters;
import net.oschina.app.api.entity.UserInfo;
import net.oschina.app.improve.account.AccountHelper;
import net.oschina.app.improve.bean.SignUpEventOptions;
import net.oschina.app.improve.bean.simple.About;
import net.oschina.app.improve.detail.sign.StringParams;
import net.oschina.app.improve.tweet.service.YouPaiResult;
import net.oschina.app.improve.write.Blog;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

import static net.oschina.app.api.LeanCloudApiHttpClient.post;

/**
 * LeanCloud api v1
 */
@SuppressWarnings("all")
public class LeanCloudApi {

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
     * 获取资讯详情
     */
    public static void getArticleDetail(String key, String ident, int type, TextHttpResponseHandler handler) {
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        paramsTmp.put("key", key);
        paramsTmp.put("ident", ident);
        paramsTmp.put("table", "ArticleDetail");
        String partUrl = "cloudQuery";
        RequestParams params = buildCqueryParams4ArticleDetail(paramsTmp);
        LeanCloudApiHttpClient.get(partUrl, params, handler);
    }


    /**
     * 获取资讯
     *
     * @param ident     手机唯一标示
     * @param pageToken pageToken
     * @param handler   handler
     */
    public static void getArticles(String ident, int type, String pageToken, TextHttpResponseHandler handler) {
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        //RequestParams params = new RequestParams();
        paramsTmp.put("ident", ident);
        paramsTmp.put("type", type);
        int pageSize = 20;
        paramsTmp.put("pageSize", pageSize);
        paramsTmp.put("table", "Articles");
        if (!TextUtils.isEmpty(pageToken)) {
            paramsTmp.put("pageToken", pageToken);
        }
        String partUrl = "cloudQuery";
        RequestParams params = buildCqueryParams4ArticleList(paramsTmp);
        LeanCloudApiHttpClient.get(partUrl, params, handler);
    }

    /**
     * 新版获得各种类型详情统一接口和Model
     */
    public static void getDetail(int type, String ident, long id, TextHttpResponseHandler handler) {
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        String table = "";
        if (type == 3) //吐槽detail
        {
            table = "TucaoDetail";
        }
        else if (type == 2) //问答detail
        {
            table = "QADetail";
        }
        paramsTmp.put("table", table);
        paramsTmp.put("id", id);
        paramsTmp.put("include", "author");
        String partUrl = "cloudQuery";
        RequestParams params = buildCqueryParams4Detail(paramsTmp);
        LeanCloudApiHttpClient.get(partUrl, params, handler);
    }


    // 获取问答列表
    public static void getSubscription2(String api, String pageToken, TextHttpResponseHandler handler) {
        //RequestParams params = new RequestParams();
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        paramsTmp.put("pageToken", pageToken);
        int pageSize = 20;
        paramsTmp.put("table", "QA");
        paramsTmp.put("pageSize", pageSize);
        paramsTmp.put("include", "author");
        String partUrl = "cloudQuery";
        RequestParams params = buildCqueryParams4TucaoList(paramsTmp);
        LeanCloudApiHttpClient.get(partUrl, params, handler);
    }


    // 获取吐槽列表
    public static void getSubscription(String api, String pageToken, TextHttpResponseHandler handler) {
        //RequestParams params = new RequestParams();
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        paramsTmp.put("pageToken", pageToken);
        int pageSize = 20;
        paramsTmp.put("table", "Tucao");
        paramsTmp.put("pageSize", pageSize);
        paramsTmp.put("include", "author");
        String partUrl = "cloudQuery";
        RequestParams params = buildCqueryParams4TucaoList(paramsTmp);
        LeanCloudApiHttpClient.get(partUrl, params, handler);
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

        JSONObject entityJson = new JSONObject();
        //RequestParams paramsTmp = new RequestParams();
        //paramsTmp.setUseJsonStreamer(true);
        // LeanCloudParameters paramsTmp = new LeanCloudParameters();
        //paramsTmp.put("userObjectId", AccountHelper.getUserObjectId());
        entityJson.put("likeCount", 0);
        entityJson.put("appClient", 1);
        entityJson.put("authorId", AccountHelper.getUserId());
        entityJson.put("content", content);
        //paramsTmp.put("imageUrls", imagesUrls);
        String jsonArrayStr = "[ ";

        for(int i = 0; i < imagesUrls.size(); i++)
        {
            String url = imagesUrls.get(i);
            String fileName = "";
            if (url != null)
            {
                fileName = url.substring(url.lastIndexOf('/')+1);
            }

            int w = 0;
            int h = 0;
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
        entityJson.put("images", JSON.parseArray(jsonArrayStr));
        String pubDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        entityJson.put("pubDate", pubDate);

        JSONObject jsonPointer = new JSONObject();
        jsonPointer.put("__type", "Pointer");
        jsonPointer.put("className", "UserInfo");
        jsonPointer.put("objectId", AccountHelper.getUserObjectId());
        entityJson.put("author", jsonPointer);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("comment", 0);
        jsonObj.put("favCount", 0);
        jsonObj.put("like", 0);
        jsonObj.put("transmit", 0);
        jsonObj.put("view", 0);

        //String statistics = jsonObj.toString();
        entityJson.put("statistics", jsonObj);
        entityJson.put("commentCount", 0);
        entityJson.put("liked", false);
        entityJson.put("href", "");
        String partUrl = "NewTweets";
        StringEntity stringEntity = null;
        try{
            stringEntity  = new StringEntity(entityJson.toString(), "UTF-8");
        }
        catch (Exception e)
        {
            throw new NullPointerException("nothing in tweet!");
        }
        //RequestParams params = buildCqueryParams4TweetCmtList(paramsTmp);
        //paramsTmp.put("audio", audioToken);
        LeanCloudApiHttpClient.post(partUrl, stringEntity, handler);
    }

    public static void getTweetLikeList(long sourceId, String pageToken, TextHttpResponseHandler handler) {
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        paramsTmp.put("tweetId", sourceId);
        paramsTmp.put("pageToken", pageToken);
        int pageSize = 20;
        paramsTmp.put("table", "TweetLikes");
        paramsTmp.put("pageSize", pageSize);
        paramsTmp.put("include", "author");
        String partUrl = "cloudQuery";
        RequestParams params = buildCqueryParams4TweetCmtList(paramsTmp);
        LeanCloudApiHttpClient.get(partUrl, params, handler);
    }


    public static void getTweetCommentList(long sourceId, String pageToken, TextHttpResponseHandler handler) {
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        paramsTmp.put("tweetId", sourceId);
        paramsTmp.put("pageToken", pageToken);

        int pageSize = 20;
        paramsTmp.put("table", "TweetComments");
        paramsTmp.put("pageSize", pageSize);
        paramsTmp.put("include", "author");
        String partUrl = "cloudQuery";
        RequestParams params = buildCqueryParams4TweetCmtList(paramsTmp);
        LeanCloudApiHttpClient.get(partUrl, params, handler);
    }


    /**
     * 动弹列表
     *
     * @param aid     author id, 请求某人的动弹
     * @param tag     相关话题
     * @param type    1: 广场（所有动弹）， 2：朋友圈（好友动弹）
     * @param order   1: 最新， 2：最热
     * @param handler handler
     */

    public static void getTweetList(Long aid, String tag, Integer type, Integer order
            , String pageToken, TextHttpResponseHandler handler) {
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        if (aid != null) {
            paramsTmp.put("authorId", aid);
        } else if (!TextUtils.isEmpty(tag)) {
            paramsTmp.put("tag", tag);
        } else {
            paramsTmp.put("type", type);
        }
        //params.put("order", order);
        paramsTmp.put("pageToken", pageToken);
        int pageSize = 20;
        paramsTmp.put("pageSize", pageSize);
        paramsTmp.put("include", "author");
        if (order == 1)
        {
            paramsTmp.put("table", "NewTweets");
        }
        else
        {
            paramsTmp.put("table", "HotTweets");
        }
        RequestParams params = buildCqueryParams4TweetList(paramsTmp);
        String partUrl = "cloudQuery";
        LeanCloudApiHttpClient.get(partUrl, params, handler);
    }

    /**
     * login account
     *
     * @param username username
     * @param pwd      pwd
     * @param handler  handler
     */
    public static void login(String username, String pwd, TextHttpResponseHandler handler) {
        LeanCloudParameters paramsTmp = new LeanCloudParameters();
        paramsTmp.put("username", username);
        paramsTmp.put("password", pwd);
        RequestParams params = new RequestParams();
        setLeanCloudHttpParam(paramsTmp, params);
        params.put("include", "identity, statistics, more");
        LeanCloudApiHttpClient.get("UserInfo", params, handler);
    }


    private static void setLeanCloudHttpParam(LeanCloudParameters paramsTmp, RequestParams params) {

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        Iterator it = paramsTmp.keySet().iterator();
        sb.append("{");
        while(it.hasNext()) {
            String key = (String)it.next();
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            Object value = paramsTmp.get(key);
            if (value instanceof String) {
                String param = (String)value;
                if (!TextUtils.isEmpty(param)) {
                    try {
                        sb.append("\"" + key + "\"" + ":" + "\"" + param + "\"");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        sb.append("}");
        String str = sb.toString();
        JSONObject jsonObject = JSON.parseObject(str);
        String conStr = "";
        try {
            conStr=jsonObject.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        params.put("where", conStr);
        Log.i("encodeUrl", conStr);

        //params.put("objectId", aid);

    }


    private static RequestParams buildCqueryParams4TweetCmtList(LeanCloudParameters params) {
        Long tweetId = null;
        Object objTmp = params.get("tweetId");
        if (objTmp != null)
            tweetId = Long.parseLong((String)objTmp);

        String pageToken = null;
        objTmp = params.get("pageToken");
        if (objTmp != null)
            pageToken = (String)objTmp;

        Long pageSize = 0L;
        objTmp = params.get("pageSize");
        if (objTmp != null)
            pageSize = Long.parseLong((String)objTmp);

        int startPage = 1;
        if(pageToken != null && !(pageToken.equals("")))
        {
            startPage = Integer.parseInt(pageToken) + 1;
        }
        Long startNum = (startPage - 1) * pageSize;

        String include = "";
        objTmp = params.get("include");
        if (objTmp != null)
            include = (String)(objTmp);

        String table = null;
        objTmp = params.get("table");
        if (objTmp != null)
            table = (String)objTmp;

        String baseCql = "select";
        if (!include.equals(""))
        {
            baseCql+= " include " + include;
        }

        baseCql += ", * from " + table;
        baseCql += " where tweetId = " + tweetId;
        baseCql += " order by objectId desc";
        baseCql += " limit " + String.valueOf(startNum) + ", " +  String.valueOf(pageSize);
        RequestParams cParams = new RequestParams();
        cParams.put("cql", baseCql);
        return cParams;
    }


    // 封装leancloud cQuery请求
    // https://leancloud.cn/docs/cql_guide.html
    private static RequestParams buildCqueryParams4TweetList(LeanCloudParameters params) {
        Long authorId = null;
        Object objTmp = params.get("authorId");
        if (objTmp != null)
            authorId = (Long)(objTmp);

        String tag = null;
        objTmp = params.get("tag");
        if (objTmp != null)
            tag = (String)(objTmp);

        String pageToken = null;
        objTmp = params.get("pageToken");
        if (objTmp != null)
            pageToken = (String)objTmp;

        Long pageSize = 0L;
        objTmp = params.get("pageSize");
        if (objTmp != null)
            pageSize = Long.parseLong((String)objTmp);

        int startPage = 1;
        if(pageToken != null)
        {
            startPage = Integer.parseInt(pageToken) + 1;
        }
        Long startNum = (startPage - 1) * pageSize;

        String table = null;
        objTmp = params.get("table");
        if (objTmp != null)
            table = (String)objTmp;

        String include = "";
        objTmp = params.get("include");
        if (objTmp != null)
            include = (String)(objTmp);

        String baseCql = "select";
        if (!include.equals(""))
        {
            baseCql+= " include " + include;
        }

        baseCql += ", * from " + table;

        //baseCql += " where 1=1";
        if (tag != null)
        {
            if(!baseCql.contains("where"))
                baseCql += " where tag =" + tag;
            else
                baseCql += " and tag = " + tag;
        }
        if (authorId != null)
        {
            if(!baseCql.contains("where"))
                baseCql += " where authorId = " + authorId;
            else
                baseCql += " and authorId = " + authorId;
        }
        baseCql += " order by objectId desc";
        baseCql += " limit " + String.valueOf(startNum) + ", " +  String.valueOf(pageSize);

        RequestParams cParams = new RequestParams();
        cParams.put("cql", baseCql);
        return cParams;
    }

    private static RequestParams buildCqueryParams4TucaoList(LeanCloudParameters params) {
        String pageToken = null;
        Object objTmp = params.get("pageToken");
        if (objTmp != null)
            pageToken = (String)objTmp;

        Long pageSize = 0L;
        objTmp = params.get("pageSize");
        if (objTmp != null)
            pageSize = Long.parseLong((String)objTmp);

        int startPage = 1;
        if(pageToken != null && !(pageToken.equals("")))
        {
            startPage = Integer.parseInt(pageToken) + 1;
        }
        Long startNum = (startPage - 1) * pageSize;

        String include = "";
        objTmp = params.get("include");
        if (objTmp != null)
            include = (String)(objTmp);

        String table = null;
        objTmp = params.get("table");
        if (objTmp != null)
            table = (String)objTmp;

        String baseCql = "select";
        if (!include.equals(""))
        {
            baseCql+= " include " + include;
        }

        baseCql += ", * from " + table;
        baseCql += " order by objectId desc";
        baseCql += " limit " + String.valueOf(startNum) + ", " +  String.valueOf(pageSize);
        RequestParams cParams = new RequestParams();
        cParams.put("cql", baseCql);
        return cParams;
    }

    private static RequestParams buildCqueryParams4Detail(LeanCloudParameters params) {
        Object objTmp;
        String include = "";
        objTmp = params.get("include");
        if (objTmp != null)
            include = (String)(objTmp);

        String table = null;
        objTmp = params.get("table");
        if (objTmp != null)
            table = (String)objTmp;

        String baseCql = "select";
        if (!include.equals(""))
        {
            baseCql+= " include " + include;
        }

        baseCql += ", * from " + table;

        Long id = null;
        objTmp = params.get("id");
        if (objTmp != null)
            id = Long.parseLong((String)(objTmp));

        if (id != null)
        {
            if(!baseCql.contains("where"))
                baseCql += " where id = " + id;
            else
                baseCql += " and id = " + id;
        }

        RequestParams cParams = new RequestParams();
        cParams.put("cql", baseCql);
        return cParams;
    }

    private static RequestParams buildCqueryParams4ArticleList(LeanCloudParameters params) {
        String pageToken = null;
        Object objTmp = params.get("pageToken");
        if (objTmp != null)
            pageToken = (String)objTmp;

        Long pageSize = 0L;
        objTmp = params.get("pageSize");
        if (objTmp != null)
            pageSize = Long.parseLong((String)objTmp);

        int startPage = 1;
        if(pageToken != null && !(pageToken.equals("")))
        {
            startPage = Integer.parseInt(pageToken) + 1;
        }
        Long startNum = (startPage - 1) * pageSize;


        String include = "";
        objTmp = params.get("include");
        if (objTmp != null)
            include = (String)(objTmp);

        String table = null;
        objTmp = params.get("table");
        if (objTmp != null)
            table = (String)objTmp;

        String baseCql = "select * ";
        if (!include.equals(""))
        {
            baseCql+= ", include " + include;
        }

        baseCql += " from " + table;
        baseCql += " order by objectId desc";
        baseCql += " limit " + String.valueOf(startNum) + ", " +  String.valueOf(pageSize);
        RequestParams cParams = new RequestParams();
        cParams.put("cql", baseCql);
        return cParams;
    }

    private static RequestParams buildCqueryParams4ArticleDetail(LeanCloudParameters params) {

        String table = null;
        Object objTmp = params.get("table");
        if (objTmp != null)
            table = (String)objTmp;

        String key = null;
        objTmp = params.get("key");
        if (objTmp != null)
            key = (String)objTmp;

        String baseCql = "select * ";
        baseCql += " from " + table;
        if (key != null)
        {
            if(!baseCql.contains("where"))
                baseCql += " where key=\'" + key + "\'";
            else
                baseCql += " and key=\'" + key + "\'";
        }
        RequestParams cParams = new RequestParams();
        cParams.put("cql", baseCql);
        return cParams;
    }
}
