package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbs.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SchoolCalendarActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.calendar_img_s1)
    ImageView mCalendarImgS1;
    @Bind(R.id.calendar_img_s2)
    ImageView mCalendarImgS2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_calendar);
        ButterKnife.bind(this);
        initToolbar();

        Glide.with(this).load(R.drawable.img_calendar_s1).into(mCalendarImgS1);
        Glide.with(this).load(R.drawable.img_calendar_s2).into(mCalendarImgS2);
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
