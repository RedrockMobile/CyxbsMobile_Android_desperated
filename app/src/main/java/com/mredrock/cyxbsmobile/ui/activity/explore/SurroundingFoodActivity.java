package com.mredrock.cyxbsmobile.ui.activity.explore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.fragment.explore.SurroundingFoodFragment;
import com.mredrock.cyxbsmobile.util.LogUtils;
import com.mredrock.cyxbsmobile.util.UIUtils;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surrounding_food);

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
