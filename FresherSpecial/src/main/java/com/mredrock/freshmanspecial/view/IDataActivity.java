package com.mredrock.freshmanspecial.view;

import android.support.design.widget.TabLayout;

import com.mredrock.freshmanspecial.units.base.IBaseActivity;

/**
 * Created by zia on 17-8-4.
 */

public interface IDataActivity extends IBaseActivity {
    TabLayout getTabLayout();
    void setBack();
}
