package com.mredrock.cyxbs.freshman.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryTipsContract;
import com.mredrock.cyxbs.freshman.mvp.model.MilitaryTipsModel;
import com.mredrock.cyxbs.freshman.mvp.presenter.MilitaryTipsPresenter;

public class MilitaryTipsFragment extends Fragment implements MilitaryTipsContract.IMilitaryTipsView {
    private View parent;
    private TextView name;
    private TextView content;
    private TextView name1;
    private TextView content1;
    private MilitaryTipsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.freshman_fragment_military_tips, container, false);
        findById();
        initMvp();
        return parent;
    }

    private void findById() {
        name = parent.findViewById(R.id.freshman_military_tips_name);
        content = parent.findViewById(R.id.freshman_military_tips_content);
        name1 = parent.findViewById(R.id.freshman_military_tips_name1);
        content1 = parent.findViewById(R.id.freshman_military_tips_content1);
    }

    private void initMvp() {
        presenter = new MilitaryTipsPresenter(new MilitaryTipsModel());
        presenter.attachView(this);
        presenter.start();
    }


    @Override
    public void setData(Description data) {
        name.setText(data.getDescribe().get(0).getContent().replace(" ", ""));
        content.setText(data.getDescribe().get(1).getContent());
        name1.setText(data.getDescribe().get(2).getContent());
        content1.setText(data.getDescribe().get(3).getContent());

        content.setLineSpacing(0, 1.5f);
        content1.setLineSpacing(0, 1.5f);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
