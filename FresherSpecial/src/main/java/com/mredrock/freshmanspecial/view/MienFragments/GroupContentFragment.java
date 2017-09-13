package com.mredrock.freshmanspecial.view.MienFragments;

import android.widget.TextView;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.base.BaseFragment;

/**
 * Created by zxzhu on 2017/8/4.
 */

public class GroupContentFragment extends BaseFragment {
    private String name;
    private TextView textView;

    public GroupContentFragment(String name){
        this.name = name;
    }

    @Override
    protected void initData() {
        textView = $(R.id.group_content_name);
        textView.setText(name);
    }

    @Override
    protected int getResourceId() {
        return R.layout.special_2017_fragment_group_content;
    }
}
