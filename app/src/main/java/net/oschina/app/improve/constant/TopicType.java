package net.oschina.app.improve.constant;

/**
 * 主题等级
 * Created by xingjianfei on 2019/07/10.
 */
public class TopicType {
    public static final int POST = 0; //帖子
    public static final int COMMENT = 1; //评论
    public static final int REPLY = 2; //回复

    public TopicType(int season) {
        System.out.println("Season :" + season);
    }

    public static void main(String[] args) {
        // Here chance to paas invalid value
        TopicType constantSeason = new TopicType(1);
    }
}