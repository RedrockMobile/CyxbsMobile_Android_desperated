package com.mredrock.cyxbs.ui.activity.lost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;

/**
 * Created by wusui on 2017/2/7.
 */

public class ReleaseSucceedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_succeed);
        StatusBarUtil.setTranslucent(this, 50);
    }

}
