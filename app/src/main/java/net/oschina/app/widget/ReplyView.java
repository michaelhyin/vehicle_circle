package net.oschina.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class ReplyView extends AppCompatTextView {

    private final static String AT_HOST_PRE = "https://my.oschina.net";
    private final static String MAIN_HOST = "https://www.oschina.net";

    private boolean dispatchToParent;

    public ReplyView(Context context) {
        super(context);
        init(context);
    }

    public ReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReplyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //setHighlightColor(0);
    }

}
