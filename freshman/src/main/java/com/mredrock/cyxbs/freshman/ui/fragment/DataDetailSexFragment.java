package com.mredrock.cyxbs.freshman.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.SexProportion;
import com.mredrock.cyxbs.freshman.mvp.contract.DataDetailSexContract;
import com.mredrock.cyxbs.freshman.mvp.model.DataDetailSexModel;
import com.mredrock.cyxbs.freshman.mvp.presenter.DataDetailSexPresenter;
import com.mredrock.cyxbs.freshman.ui.widget.CircleProcessView;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

/*
 by Cynthia at 2018/8/15
 description :
 */
public class DataDetailSexFragment extends Fragment implements DataDetailSexContract.IDataDetailSexView {

    private String name;
    private CircleProcessView mCpv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.freshman_fragment_data_detail_sex, container, false);
        initView(view);
        initMVP();
        return view;
    }

    private void initView(View view) {
        mCpv = view.findViewById(R.id.cpv_data);
        TextView title = view.findViewById(R.id.tv_data_sex);
        String temp = name + getResources().getString(R.string.freshman_campus_data_detail_sex);
        title.setText(temp);
    }

    private void initMVP() {
        DataDetailSexPresenter presenter = new DataDetailSexPresenter(new DataDetailSexModel(name));
        presenter.attachView(this);
        presenter.start();
    }

    public void setData(String name) {
        this.name = name;
    }

    @Override
    public void showError(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void loadSexView(SexProportion sexProportion) {
        float total = sexProportion.getFemale_amount() + sexProportion.getMale_amount();
        int manPro = (int) (sexProportion.getMale_amount() / total * 100);
        int womanPro = 100 - manPro;
        float[] process = new float[]{womanPro, manPro};
        mCpv.setProcess(process);
    }
}
