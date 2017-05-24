package com.mredrock.cyxbs.ui.activity.explore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.fragment.explore.WhatToEatFragment;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Stormouble on 16/4/16.
 */
public class WhatToEatActivity extends BaseExploreActivity {

    private static final String TAG = LogUtils.makeLogTag(WhatToEatActivity.class);

    public static void startWhatToEatActivity(int[] startLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, WhatToEatActivity.class);
        intent.putExtra(ARG_DRAWING_START_LOCATION, startLocation);
        startingActivity.startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_eat);
        StatusBarUtil.setTranslucent(this, 50);
        int[] startLocation = getIntent().getIntArrayExtra(ARG_DRAWING_START_LOCATION);
        if (savedInstanceState == null) {
            WhatToEatFragment fragment =
                    (WhatToEatFragment) getSupportFragmentManager().findFragmentById(R.id.what_to_eat_contentFrame);
            if (fragment == null) {
                fragment = WhatToEatFragment.newInstance(startLocation);
                UIUtils.addFragmentToActivity(getSupportFragmentManager(),
                        fragment, R.id.what_to_eat_contentFrame);
            }
        } else {
            tryHandleMultiFragmentRestored(savedInstanceState);
        }

        overridePendingTransition(0, 0);
    }


}
