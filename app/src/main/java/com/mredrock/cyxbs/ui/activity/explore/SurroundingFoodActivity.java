package com.mredrock.cyxbs.ui.activity.explore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.fragment.explore.SurroundingFoodFragment;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Stormouble on 16/4/16.
 */
public class SurroundingFoodActivity extends BaseExploreActivity {

    private static final String TAG = LogUtils.makeLogTag(SurroundingFoodActivity.class);

    public static void startSurroundingFoodActivity(int[] startLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, SurroundingFoodActivity.class);
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
        setContentView(R.layout.activity_surrounding_food);
        StatusBarUtil.setTranslucent(this, 50);
        int[] startLocation = getIntent().getIntArrayExtra(ARG_DRAWING_START_LOCATION);
        if (savedInstanceState == null) {
            SurroundingFoodFragment fragment =
                    (SurroundingFoodFragment) getSupportFragmentManager().findFragmentById(R.id.surrounding_food_contentFrame);
            if (fragment == null) {
                fragment = SurroundingFoodFragment.newInstance(startLocation);
                UIUtils.addFragmentToActivity(
                        getSupportFragmentManager(), fragment, R.id.surrounding_food_contentFrame);
            }
        } else {
            tryHandleMultiFragmentRestored(savedInstanceState);
        }

        overridePendingTransition(0, 0);
    }
}
