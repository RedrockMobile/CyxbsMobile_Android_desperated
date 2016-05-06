package com.mredrock.cyxbsmobile.ui.activity.me;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.util.ImageLoader;

public class SchoolCalendarActivity extends AppCompatActivity {

    @Bind(R.id.calendar_img)
    ImageView calendarImg;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_calendar);
        ButterKnife.bind(this);
        initToolbar();
        ImageLoader.getInstance().loadLocalImage(R.drawable.img_calendar,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        calendarImg.setImageBitmap(resource);
                    }
                });
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("校历");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> SchoolCalendarActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
