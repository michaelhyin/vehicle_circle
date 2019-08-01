package net.oschina.app.improve.bean.simple;
import java.io.Serializable;

public class Reply implements Serializable {
    private long id;
    private long commentId;
    private long replyId;
    //0: 评论中评论；1：评论中回复
    private int replyType;
    private Author fromAuthor;
    private Author toAuthor;
    private String content;
    private String pubDate;
    private boolean liked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public int getReplyType() {
        return replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }

    public Author getFromAuthor() {
        return fromAuthor;
    }

    public void setFromAuthor(Author fromAuthor) {
        this.fromAuthor = fromAuthor;
    }

    public Author getToAuthor() {
        return toAuthor;
    }

    public void setToAuthor(Author toAuthor) {
        this.toAuthor = toAuthor;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", author=" + fromAuthor +
                ", content='" + content + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}
