package net.oschina.app;

import android.content.Context;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.text.TextUtils;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.baidu.mapapi.SDKInitializer;

import net.oschina.app.api.ApiHttpClient;
import net.oschina.app.api.LeanCloudApiHttpClient;
import net.oschina.app.api.entity.UserInfo;
import net.oschina.app.improve.account.AccountHelper;
import net.oschina.app.improve.base.activities.BaseActivity;
import net.oschina.app.improve.detail.db.DBManager;
import net.oschina.app.improve.detail.v2.DetailCache;
import net.oschina.app.improve.main.MainActivity;
import net.oschina.app.improve.main.update.OSCSharedPreference;
import net.oschina.app.improve.utils.MD5;
import net.oschina.app.chatkit.CustomUserProvider;
import net.oschina.common.helper.ReadStateHelper;

import java.util.UUID;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by qiujuer
 * on 2016/10/27.
 */
public class OSCApplication extends AppContext {
    private static final String CONFIG_READ_STATE_PRE = "CONFIG_READ_STATE_PRE_";


    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化操作
        DetailCache.init(getApplicationContext());
        init();
        imInit();
    }

    public static void reInit() {
        ((OSCApplication) OSCApplication.getInstance()).init();
    }

    private void init() {
        BaseActivity.IS_ACTIVE = true;
        OSCSharedPreference.init(this, "osc_update_sp");
        if (TextUtils.isEmpty(OSCSharedPreference.getInstance().getDeviceUUID())) {
            String androidId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
            if (TextUtils.isEmpty(androidId)) {
                androidId = UUID.randomUUID().toString().replaceAll("-", "");
            }
            OSCSharedPreference.getInstance().putDeviceUUID(MD5.get32MD5Str(androidId));
        }
        // 初始化异常捕获类
        //AppCrashHandler.getInstance().init(this);
        // 初始化账户基础信息
        AccountHelper.init(this);
        // 初始化网络请求
        ApiHttpClient.init(this);
        LeanCloudApiHttpClient.init(this);
        //初始化百度地图
        SDKInitializer.initialize(this);
        DBManager.init(this);
        AVObject.registerSubclass(UserInfo.class);
        AVOSCloud.initialize(this, BuildConfig2.LEANCLOUD_APPID,
                BuildConfig2.LEANCLOUD_APPKEY);

        if (OSCSharedPreference.getInstance().hasShowUpdate()) {//如果已经更新过
            //如果版本大于更新过的版本，就设置弹出更新
            if (BuildConfig2.VERSION_CODE > OSCSharedPreference.getInstance().getUpdateVersion()) {
                OSCSharedPreference.getInstance().putShowUpdate(true);
            }
        }
    }

    private void imInit() {
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        AVOSCloud.setDebugLogEnabled(true);
//    AVOSCloud.useAVCloudUS();
        LCChatKit.getInstance().init(getApplicationContext(), BuildConfig2.LEANCLOUD_APPID, BuildConfig2.LEANCLOUD_APPKEY);
        AVIMClient.setAutoOpen(true);
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.setAutoWakeUp(true);
        PushService.setDefaultChannelId(this, "default");

        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    System.out.println("---  " + installationId);
                } else {
                    // 保存失败，输出错误信息
                    System.out.println("failed to save installation.");
                }
            }
        });
    }
    /**
     * 获取已读状态管理器
     *
     * @param mark 传入标示，如：博客：blog; 新闻：news
     * @return 已读状态管理器
     */
    public static ReadState getReadState(String mark) {
        ReadStateHelper helper = ReadStateHelper.create(getInstance(),
                CONFIG_READ_STATE_PRE + mark, 100);
        return new ReadState(helper);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 一个已读状态管理器
     */
    public static class ReadState {
        private ReadStateHelper helper;

        ReadState(ReadStateHelper helper) {
            this.helper = helper;
        }

        /**
         * 添加已读状态
         *
         * @param key 一般为资讯等Id
         */
        public void put(long key) {
            helper.put(key);
        }

        /**
         * 添加已读状态
         *
         * @param key 一般为资讯等Id
         */
        public void put(String key) {
            helper.put(key);
        }

        /**
         * 获取是否为已读
         *
         * @param key 一般为资讯等Id
         * @return True 已读
         */
        public boolean already(long key) {
            return helper.already(key);
        }

        /**
         * 获取是否为已读
         *
         * @param key 一般为资讯等Id
         * @return True 已读
         */
        public boolean already(String key) {
            return helper.already(key);
        }
    }
}
