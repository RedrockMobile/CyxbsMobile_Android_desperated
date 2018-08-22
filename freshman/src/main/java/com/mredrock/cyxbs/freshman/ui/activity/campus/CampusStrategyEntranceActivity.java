package com.mredrock.cyxbs.freshman.ui.activity.campus;

/*
 只是简单的页面跳转Activity，没有使用mvp进行操作
 */

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.ui.activity.BaseActivity;
import com.mredrock.cyxbs.freshman.ui.activity.strategy.SameStrategyMVPActivityKt;
import com.mredrock.cyxbs.freshman.ui.adapter.EntranceAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_BANK;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_BUS;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_CANTEEN;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_CATE;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_DORMITORY;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_ENVIRONMENT;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_EXPRESS;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_REVEAL;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_SCENIC;
import static com.mredrock.cyxbs.freshman.utils.net.Const.INDEX_STRATEGY;

public class CampusStrategyEntranceActivity extends BaseActivity {

    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRv = findViewById(R.id.rv_entrance);
        initData();
    }

    private void initData() {
        List<SimpleData> datas = new ArrayList<>();
        String[] names = {INDEX_CANTEEN, INDEX_DORMITORY, INDEX_CATE,
                INDEX_SCENIC, INDEX_ENVIRONMENT, INDEX_REVEAL,
                INDEX_BANK, INDEX_BUS, INDEX_EXPRESS};
        @IdRes
        int[] ids = {R.drawable.freshman_icon_canteen, R.drawable.freshman_icon_dormitory, R.drawable.freshman_icon_cate,
                R.drawable.freshman_icon_scenic, R.drawable.freshman_icon_environment, R.drawable.freshman_icon_data,
                R.drawable.freshman_icon_bank, R.drawable.freshman_icon_bus, R.drawable.freshman_icon_express};
        for (int i = 0; i < names.length; i++) {
            SimpleData simpleData = new SimpleData();
            simpleData.setName(names[i]);
            simpleData.setId(ids[i]);
            datas.add(simpleData);
        }
        EntranceAdapter simpleAdapter = new EntranceAdapter(datas, name -> {
            SameStrategyMVPActivityKt.createStrategyActivity(CampusStrategyEntranceActivity.this, name);
        });
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRv.setLayoutManager(manager);
        mRv.setAdapter(simpleAdapter);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.freshman_activity_campus_strategy_entrance;
    }

    @NotNull
    @Override
    public String getToolbarTitle() {
        return INDEX_STRATEGY;
    }

    public class SimpleData {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(@IdRes int id) {
            this.id = id;
        }
    }

}
