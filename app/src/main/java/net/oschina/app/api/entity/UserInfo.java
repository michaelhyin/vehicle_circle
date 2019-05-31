package net.oschina.app.api.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by 大灯泡 on 2016/10/27.
 * <p>
 * 用户
 */
@AVClassName("UserInfo")
public class UserInfo extends AVObject {
    public static final Creator CREATOR = AVObjectCreator.instance;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NICK = "nick";
    public static final String AVATAR = "avatar";
    public static final String COVER = "cover";

//    public interface UserFields {
//        String USERNAME = "username";
//        String PASSWORD = "password";
//        String NICK = "nick";
//        String AUTHOR_USER = "author";
//        String AVATAR = "avatar";
//        String COVER="cover";
//    }

    private String username;
    private String password;
    private String nick;
    private String avatar;
    private String cover;
    private String __type;
    public UserInfo() {
    }

    public String getUsername() {
        return getString(USERNAME);
    }

    public void setUsername(String username) {
        this.username = username;
        put(USERNAME, username);
    }

    public String getUserId() {
        return getObjectId();
    }

    public String getPassword() {
        return getString(PASSWORD);
    }

    public String getType() {
        return this.__type;
    }

    public void setType(String type) {
        this.__type = type ;
    }


    public void setNick(String nick) {
        this.nick = nick;
        put(NICK, nick);
    }

    public String getAvatar() {
        return getString(AVATAR);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        put(AVATAR, avatar);
    }

    public String getCover() {
        return getString(COVER);
    }

    public void setCover(String cover) {
        this.cover = cover;
        put(COVER, cover);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
