package com.mredrock.cyxbs.freshman.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.SubjectProportion;
import com.mredrock.cyxbs.freshman.mvp.contract.DataDetailSubjectContract;
import com.mredrock.cyxbs.freshman.mvp.model.DataDetailSubjectModel;
import com.mredrock.cyxbs.freshman.mvp.presenter.DataDetailSubjectPresenter;
import com.mredrock.cyxbs.freshman.ui.widget.RectProcessView;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

/*
 by Cynthia at 2018/8/16
 description : 
 */
public class DataDetailSubjectFragment extends Fragment implements DataDetailSubjectContract.IDataDetailSubjectView {

    private int max;
    private float time;
    private String name;
    private int[] personNum;
    private String[] subjectName;

    private boolean rectFirst;

    private RectProcessView mRpv;
    private View rootView;

    public void setName(String name) {
        this.name = name;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.freshman_fragment_data_detail_subject, container, false);
        rectFirst = true;
        rootView = view;
        initView(view);
        initMVP();

        return view;
    }

    private void initMVP() {
        DataDetailSubjectPresenter presenter = new DataDetailSubjectPresenter(new DataDetailSubjectModel(name));
        presenter.attachView(this);
        presenter.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null)
            return;
        if (rectFirst && isVisibleToUser) {
            if (subjectName == null && personNum == null)
                return;
            mRpv.setSubject(subjectName);
            mRpv.setMax(max);
            mRpv.setAnim(personNum, time);
            rectFirst = false;
        }
    }

    private void initView(View view) {
        mRpv = view.findViewById(R.id.rpv_data);
        TextView title = view.findViewById(R.id.tv_data_subject);
        title.setText(getResources().getString(R.string.freshman_campus_data_title_subject));
    }

    @Override
    public void showError(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void loadSubjectView(SubjectProportion subjectProportion) {
        int num = subjectProportion.getArray().size();
        subjectName = new String[num];
        personNum = new int[num];
        for (int i = 0; i < num; i++) {
            subjectName[i] = subjectProportion.getArray().get(i).getSubject_name();
            personNum[i] = subjectProportion.getArray().get(i).getBelow_amount();
            if (personNum[i] > max) {
                max = personNum[i];
            }
        }
        int temp = 120;
        while (temp < max) {
            temp = temp + 30;
        }
        while (temp - max > 45) {
            temp = temp - 30;
        }
        time = temp <= 60 ? 0.5f : 2f;
        time = temp <= 30 ? 0.2f : 2f;
        if (temp - max < temp / 6 / 3 && temp != 120)
            temp = temp + 30;
        max = temp;
    }
}
