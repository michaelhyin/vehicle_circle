package net.oschina.app.api;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.util.Date;
import java.text.SimpleDateFormat;


public class LeanCloudPatternOpt {
    public static String getLoginPatternStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        jsonObject.remove("results");
        JSONObject object = new JSONObject();
        object.put("result",ja.get(0));
        object.put("code",1);
        object.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        object.put("time", df.format(new Date()));
        return object.toString();
    }

    public static String getTweetsPatternStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        JSONObject object = new JSONObject();
        object.put("items",ja);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", object);
        totalObject.put("requestCount", 20);
        totalObject.put("responseCount", 20);
        totalObject.put("totalResults", 1000);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));

        return totalObject.toString();
    }

    public static String getTweetCmtPatternStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        JSONObject object = new JSONObject();
        object.put("items",ja);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", object);
        totalObject.put("requestCount", 20);
        totalObject.put("responseCount", 20);
        totalObject.put("totalResults", 1000);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));

        return totalObject.toString();
    }
    public static String getTweetLikesPatternStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        JSONObject object = new JSONObject();
        object.put("items",ja);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", object);
        totalObject.put("requestCount", 20);
        totalObject.put("responseCount", 20);
        totalObject.put("totalResults", 1000);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));

        return totalObject.toString();
    }

    public static String getPubTweetPatternStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", jsonObject);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));

        return totalObject.toString();
    }

    public static String getTucaoListStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        JSONObject object = new JSONObject();
        object.put("items",ja);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", object);
        totalObject.put("requestCount", 20);
        totalObject.put("responseCount", 20);
        totalObject.put("totalResults", 1000);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));
        return totalObject.toString();
    }

    public static String getTucaoDetailStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        String str = (ja.get(0)).toString();
        JSONObject strJson = JSON.parseObject(str);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", strJson);
        totalObject.put("requestCount", 20);
        totalObject.put("responseCount", 20);
        totalObject.put("totalResults", 1000);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));
        return totalObject.toString();
    }
    public static String getArticlesPatternStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        JSONObject object = new JSONObject();
        object.put("items",ja);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", object);
        totalObject.put("requestCount", 20);
        totalObject.put("responseCount", 20);
        totalObject.put("totalResults", 1000);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));

        return totalObject.toString();
    }

    public static String getArticleDetailStr(String responseString)
    {
        JSONObject jsonObject = JSON.parseObject(responseString);
        String value = jsonObject.getString("results");
        JSONArray ja = JSON.parseArray(value);
        String str = (ja.get(0)).toString();
        JSONObject strJson = JSON.parseObject(str);
        JSONObject totalObject = new JSONObject();
        totalObject.put("result", strJson);
        totalObject.put("requestCount", 20);
        totalObject.put("responseCount", 20);
        totalObject.put("totalResults", 1000);
        totalObject.put("code",1);
        totalObject.put("message", "SUCCESS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        totalObject.put("time", df.format(new Date()));
        return totalObject.toString();
    }

}
