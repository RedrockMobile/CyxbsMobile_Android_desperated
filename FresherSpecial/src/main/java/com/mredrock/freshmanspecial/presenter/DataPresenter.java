package com.mredrock.freshmanspecial.presenter;

import com.mredrock.freshmanspecial.model.TabModel;
import com.mredrock.freshmanspecial.view.DataActivity;
import com.mredrock.freshmanspecial.view.IDataActivity;

/**
 * Created by zia on 17-8-4.
 */

public class DataPresenter implements IDataPresenter {

    private IDataActivity activity;
    private TabModel model;

    public DataPresenter(DataActivity activity) {
        this.activity = activity;
        model = new TabModel();
    }

    /**
     * 设置下划线边距
     * @param padding px
     */
    @Override
    public void setTabLayoutBottomLine(final int padding) {
        activity.getTabLayout().post(new Runnable() {
            @Override
            public void run() {
                model.setIndicator(activity.getTabLayout(),padding,padding);
            }
        });
    }


}
