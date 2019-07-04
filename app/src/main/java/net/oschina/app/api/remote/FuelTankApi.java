package net.oschina.app.api.remote;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import net.oschina.app.api.ApiHttpClient;
import net.oschina.app.api.FuelTankApiHttpClient;
import net.oschina.app.api.LeanCloudApiHttpClient;
import net.oschina.app.api.LeanCloudParameters;
import net.oschina.app.improve.account.AccountHelper;
import net.oschina.app.improve.bean.simple.About;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.entity.StringEntity;

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
        FuelTankApiHttpClient.get("vehicle_circle/tweet/tweet_list", params, handler);
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
        params.put("content", content);
        params.put("imagesUrls", imagesUrls);
//        params.put("audio", audioToken);
//        if (About.check(share)) {
//            params.put("aboutId", share.id);
//            params.put("aboutType", share.type);
//            params.put("aboutFromTweetId", share.fromTweetId);
//        }
        FuelTankApiHttpClient.post("vehicle_circle/tweet/publish", params, handler);
    }


}
