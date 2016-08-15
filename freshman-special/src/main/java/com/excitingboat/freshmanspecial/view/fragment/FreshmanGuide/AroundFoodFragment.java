package com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.model.bean.PlaceWithIntroduction;
import com.excitingboat.freshmanspecial.net.GetInformation;
import com.excitingboat.freshmanspecial.presenter.GetInformationPresenter;
import com.excitingboat.freshmanspecial.view.adapter.AroundFoodAdapter;
import com.excitingboat.freshmanspecial.view.iview.IGetInformation;

import java.util.List;

/**
 * Created by xushuzhan on 2016/8/14.
 */
public class AroundFoodFragment extends Fragment implements IGetInformation<PlaceWithIntroduction> {

    private RecyclerView recyclerview;
    GetInformationPresenter<PlaceWithIntroduction> presenter;
    private AroundFoodAdapter adapter;
    private int currentPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (recyclerview == null) {
            recyclerview = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.project_freshman_special__recyclerview, container, false);
            presenter = new GetInformationPresenter<>(this, GetInformation.FOOD);
            adapter = new AroundFoodAdapter(this);
        }
        currentPage = 0;
        return recyclerview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter);
        presenter.getInformation(new int[]{currentPage, 15});
    }

    @Override
    public void requestSuccess(List<PlaceWithIntroduction> list) {
        if (list.size() > 0) {
            adapter.addAll(list);
            presenter.getInformation(new int[]{++currentPage, 15});
        }
    }

    @Override
    public void requestFail() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unBind();
    }
}
