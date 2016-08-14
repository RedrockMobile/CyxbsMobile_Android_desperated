package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SchoolCalendarActivity extends AppCompatActivity {

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
