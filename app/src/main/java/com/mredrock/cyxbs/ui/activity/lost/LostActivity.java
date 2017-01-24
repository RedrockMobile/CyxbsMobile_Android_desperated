package com.mredrock.cyxbs.ui.activity.lost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostActivity extends BaseActivity {

    @Bind(R.id.toolbar) RelativeLayout toolbar;
    @Bind(R.id.rb_lost) RadioButton chooseModeLost;
    @Bind(R.id.rb_found) RadioButton chooseModeFound;

    public static final int MODE_LOST = 0;
    public static final int MODE_FOUND = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this, 50);
    }

    private void refreshMode() {

    }

    private int getMode() {
        if (chooseModeLost.isChecked()) {
            return MODE_LOST;
        } else {
            return MODE_FOUND;
        }
    }

    @OnClick(R.id.bt_navigation_up)
    public void onNavigationUpClick() {
        finish();
    }

}
