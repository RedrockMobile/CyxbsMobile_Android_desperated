package com.mredrock.cyxbs.freshman.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.StrategyData;
import com.mredrock.cyxbs.freshman.mvp.contract.ReportingProcessContract;
import com.mredrock.cyxbs.freshman.mvp.model.ReportingProcessModel;
import com.mredrock.cyxbs.freshman.mvp.presenter.ReportingProcessPresenter;
import com.mredrock.cyxbs.freshman.ui.adapter.ReportingProcessAdapter;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import org.jetbrains.annotations.NotNull;

public class ReportingProcessActivity extends BaseActivity implements ReportingProcessContract.IReportingProcessView {

    private ReportingProcessAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMVP();
    }

    private void initMVP() {
        ReportingProcessPresenter mPresenter = new ReportingProcessPresenter(new ReportingProcessModel());
        mPresenter.attachView(this);
        mPresenter.start();
    }

    @Override
    public void showError(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void setData(StrategyData data) {
        mAdapter = new ReportingProcessAdapter(data.getDetails(),
                ((pos) -> expandItem(mAdapter.getList().get(pos))), this);
        RecyclerView mRv = findViewById(R.id.rv_report);
        mRv.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRv.setAdapter(mAdapter);
        mRv.getItemAnimator().setChangeDuration(100);
        mRv.getItemAnimator().setMoveDuration(200);
    }

    @Override
    public void expandItem(StrategyData.DetailData detailData) {
        Intent intent = new Intent(ReportingProcessActivity.this, ReportingProcessMoreActivity.class);
        intent.putExtra("data", detailData);
        startActivity(intent);
        overridePendingTransition(R.anim.freshman_anim_in, R.anim.freshman_anim_out);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.freshman_activity_reporting_process;
    }

    @NotNull
    @Override
    public String getToolbarTitle() {
        return Const.INDEX_REGISTRATION;
    }
}
