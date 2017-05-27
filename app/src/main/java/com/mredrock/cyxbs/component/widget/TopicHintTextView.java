package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

/**
 * Created by simonla on 2017/4/14.
 * 15:06
 */

public class TopicHintTextView extends android.support.v7.widget.AppCompatTextView {

    private int a = 0, b = 0;

    public TopicHintTextView(Context context) {
        super(context);
    }

    public TopicHintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopicHintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
        try {
            if (hasTopic(text)) {
                SpannableString spanned = new SpannableString(text);
                spanned.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),
                        R.color.colorAccent)), a, b, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                setText(spanned, TextView.BufferType.SPANNABLE);
            } else {
                setText(text);
            }
        } finally {
            a = 0;
            b = 0;
        }
    }

    private boolean hasTopic(String text) {
        boolean perMatched = false;
        for (int i = 0; i < text.length(); i++) {
            if (a != b) break;
            if (text.charAt(i) == '#') {
                if (!perMatched) {
                    a = i;
                    perMatched = true;
                } else {
                    b = i + 1;
                }
            }
        }
        return a != b;
    }
}
