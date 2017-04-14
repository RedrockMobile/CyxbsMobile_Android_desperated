package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by simonla on 2017/4/14.
 * 15:06
 */

public class TopicHintTextView extends android.support.v7.widget.AppCompatTextView {

    public TopicHintTextView(Context context) {
        super(context);
    }

    public TopicHintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopicHintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }
}
