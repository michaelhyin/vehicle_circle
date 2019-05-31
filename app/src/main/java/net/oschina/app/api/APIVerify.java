package net.oschina.app.api;

import net.oschina.app.BuildConfig2;
import net.oschina.app.improve.utils.AES;
import net.oschina.app.improve.utils.MD5;

/**
 * API验证
 * Created by huanghaibin on 2018/4/13.
 */

public final class APIVerify {

    public static String getVerifyString() {
        if (BuildConfig2.DEBUG) {
            return MD5.get32MD5Str(AES.encryptByBase64(BuildConfig2.APPLICATION_ID));
        }
        return MD5.get32MD5Str(MD5.get32MD5Str(BuildConfig2.AES_KEY) + MD5.get32MD5Str(BuildConfig2.VERSION_NAME) + AES.encryptByBase64(BuildConfig2.APPLICATION_ID));
    }

}
