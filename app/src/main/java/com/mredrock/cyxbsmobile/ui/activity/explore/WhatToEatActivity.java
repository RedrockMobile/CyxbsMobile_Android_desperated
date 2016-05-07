package com.mredrock.cyxbsmobile.ui.activity.explore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.fragment.explore.SurroundingFoodFragment;
import com.mredrock.cyxbsmobile.ui.fragment.explore.WhatToEatFragment;
import com.mredrock.cyxbsmobile.util.LogUtils;
import com.mredrock.cyxbsmobile.util.UIUtils;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_eat);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                tryHandleMultiFragmentBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        tryHandleMultiFragmentBack();
    }
}
