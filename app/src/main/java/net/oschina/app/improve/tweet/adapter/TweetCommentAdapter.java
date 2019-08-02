package net.oschina.app.improve.tweet.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import net.oschina.app.R;
import net.oschina.app.bean.Comment;
import net.oschina.app.emoji.InputHelper;
import net.oschina.app.improve.base.adapter.BaseRecyclerAdapter;
import net.oschina.app.improve.bean.simple.Reply;
import net.oschina.app.improve.bean.simple.TweetComment;
import net.oschina.app.improve.widget.IdentityView;
import net.oschina.app.improve.widget.PortraitView;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.UIHelper;
import net.oschina.app.widget.ReplyView;
import net.oschina.app.widget.TweetTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.tencent.open.utils.Global.getContext;

/**
 * Created by thanatos
 * on 16/6/13.
 */
public class TweetCommentAdapter extends BaseRecyclerAdapter<TweetComment> {

    private RequestManager reqManager;
    private View.OnClickListener onPortraitClickListener;

    public TweetCommentAdapter(Context context) {
        super(context, ONLY_FOOTER);
        reqManager = Glide.with(context);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new TweetCommentHolderView(LayoutInflater.from(mContext).inflate(R.layout.list_item_tweet_comment_pro, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, TweetComment item, int position) {
        TweetCommentHolderView h = (TweetCommentHolderView) holder;

        h.identityView.setup(item.getAuthor());
        h.ivPortrait.setup(item.getAuthor());
        h.ivPortrait.setTag(R.id.iv_tag, item);
        h.ivPortrait.setOnClickListener(getOnPortraitClickListener());

        h.tvName.setText(item.getAuthor().getName());
        h.tvContent.setText(InputHelper.displayEmoji(mContext.getResources(), item.getContent()));
        h.tvTime.setText(StringUtils.formatSomeAgo(item.getPubDate()));
        if (item.getReplyNum() > 0)
        {
            Reply[] replies = item.getReplies();
            SpannableStringBuilder span = new SpannableStringBuilder("");
            for (int i = 0; i < replies.length; i++)
            {
                //https://blog.csdn.net/xudailong_blog/article/details/86513668
                Reply reply = replies[i];
                String fromAuthorName = reply.getFromAuthor().getName();
                SpannableStringBuilder span1 = new SpannableStringBuilder(fromAuthorName);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.follow_text_enable_color));
                span1.setSpan(foregroundColorSpan, 0, fromAuthorName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //SpannableString spannableString = EmojiConversionUtils.getInstance().getExpressionString(context, spannableStr);

                SpannableString spannableString;
                if (reply.getReplyType() == 1) //当评论下回复为回复的回复时
                {
                    spannableString = new SpannableString(" 回复 ");
                    span1.append(spannableString);
                    SpannableStringBuilder span2 = new SpannableStringBuilder(reply.getToAuthor().getName());
                    span2.setSpan(foregroundColorSpan, 0, fromAuthorName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span1.append(span2);
                }
                spannableString = new SpannableString(": " + reply.getContent()+"\n");
                span1.append(spannableString);
                span.append(span1);
            }
            String lastStr = "查看全部回复 " + String.valueOf(item.getReplyNum()) + " 条";
            SpannableStringBuilder spanLast = new SpannableStringBuilder(lastStr);
            span.append(spanLast);
            h.replyView.setText(span);
            h.replyView.setVisibility(View.VISIBLE);
        }
        else
            h.replyView.setVisibility(View.INVISIBLE);
    }

    private View.OnClickListener getOnPortraitClickListener() {
        if (onPortraitClickListener == null) {
            onPortraitClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj = v.getTag(R.id.iv_tag);
                    if (obj != null && obj instanceof TweetComment) {
                        TweetComment comment = (TweetComment) obj;
                        UIHelper.showUserCenter(mContext, comment.getAuthor().getId(), comment.getAuthor().getName());
                    }
                }
            };
        }
        return onPortraitClickListener;
    }

    public static final class TweetCommentHolderView extends RecyclerView.ViewHolder {
        @Bind(R.id.identityView)
        public IdentityView identityView;
        @Bind(R.id.iv_avatar)
        public PortraitView ivPortrait;
        @Bind(R.id.tv_name)
        public TextView tvName;
        @Bind(R.id.tv_pub_date)
        public TextView tvTime;
        @Bind(R.id.btn_comment)
        public ImageView btnReply;
        @Bind(R.id.tv_content)
        public TweetTextView tvContent;
        @Bind(R.id.reply)
        public ReplyView replyView;

        public TweetCommentHolderView(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
