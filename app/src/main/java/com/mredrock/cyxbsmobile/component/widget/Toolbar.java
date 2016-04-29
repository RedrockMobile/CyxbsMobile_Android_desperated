package com.mredrock.cyxbsmobile.component.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbsmobile.R;

/**
 * Created by skylineTan on 2016/4/27 01:35.
 */
public class Toolbar extends FrameLayout{

    private TextView right,left,title;
    private CharSequence rightText,leftText,titleText;

    public Toolbar(Context context) {
        this(context,null);
    }


    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setLeftTextListener(OnClickListener listener){
        left.setOnClickListener(listener);
    }

    public void setRightTextListener(OnClickListener listener){
        right.setOnClickListener(listener);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout
                .toolbar_save_cancel,this);
        right = (TextView) view.findViewById(R.id.save);
        left = (TextView) view.findViewById(R.id.cancel);
        title = (TextView) view.findViewById(R.id.title);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable
                .Toolbar);
        titleText = a.getText(R.styleable.Toolbar_text_title);
        title.setText(a.getText(R.styleable.Toolbar_text_title));
        if(a.getText(R.styleable.Toolbar_text_left) != null) {
            leftText = a.getText(R.styleable.Toolbar_text_left);
            left.setText(leftText);
        }
        if(a.getText(R.styleable.Toolbar_text_right) != null) {
            rightText = a.getText(R.styleable.Toolbar_text_right);
            right.setText(rightText);
        }
        a.recycle();
    }
}
