package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.mredrock.cyxbs.R;


/**
 * Created by simonla on 2017/4/12.
 * 12:47
 */

public class TopicEditText extends android.support.v7.widget.AppCompatEditText {

    public static final String TAG = TopicEditText.class.getSimpleName();
    private OnTopicEditListener mListener;
    private int mTitleLength;
    private String mTitle;

    public TopicEditText(Context context) {
        super(context);
    }

    public TopicEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopicEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTopicEditListener(OnTopicEditListener listener) {
        mListener = listener;
    }

    public void setTopicText(String s) {
        mListener.onTopic();
        mTitleLength = s.length();
        mTitle = s;
        SpannableString spanned = new SpannableString(s);
        spanned.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorAccent)), 0, mTitleLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spanned, BufferType.SPANNABLE);
        setSelection(mTitleLength);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (getEditableText().toString().equals(mTitle)) {
                selectAll();
                mTitle = "";
                mTitleLength = -1;
                mListener.onNoTopic();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface OnTopicEditListener {
        void onTopic();

        void onNoTopic();
    }
}
