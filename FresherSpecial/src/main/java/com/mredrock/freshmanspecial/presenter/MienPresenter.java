package com.mredrock.freshmanspecial.presenter;

import com.mredrock.freshmanspecial.model.TabModel;
import com.mredrock.freshmanspecial.view.MienActivity;

/**
 * Created by zxzhu on 2017/8/4.
 */

public class MienPresenter implements IMienPresenter {
    private TabModel model;
    private MienActivity activity;

    public MienPresenter(MienActivity activity) {
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
