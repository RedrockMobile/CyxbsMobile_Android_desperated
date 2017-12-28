package com.mredrock.cyxbs.ui.activity.explore;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.mredrock.cyxbs.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SchoolCarLearnMore extends AppCompatActivity {
    @BindView(R.id.learn_more_school_car_toolbar)
    Toolbar toolbar;
    @BindView(R.id.learn_more_toolbar_back)
    ImageView back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_car_learn_more);
        ButterKnife.bind(this);
        initToolBar();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }

    @OnClick(R.id.learn_more_toolbar_back)
    public void back(View v) {
        if (v.getId() == R.id.learn_more_toolbar_back) {
            finish();
        }
    }
}
