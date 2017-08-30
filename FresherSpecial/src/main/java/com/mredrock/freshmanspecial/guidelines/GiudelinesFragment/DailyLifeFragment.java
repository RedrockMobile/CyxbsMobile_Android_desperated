package com.mredrock.freshmanspecial.guidelines.GiudelinesFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.beans.DailyLifeBean;
import com.mredrock.freshmanspecial.beans.GuidelinesHorizontalBean;
import com.mredrock.freshmanspecial.guidelines.Adapter.DailyLifeRecyclerAdapter;
import com.mredrock.freshmanspecial.model.HttpModel;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by Glossimar on 2017/8/3.
 * 专题 ： 邮子攻略
 * 界面 ： 邮子攻略-日常生活
 */

public class DailyLifeFragment extends Fragment {
    private List<GuidelinesHorizontalBean> shopBeanList;
    private RecyclerView recyclerView;
    private DailyLifeRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_2017_fragment_daliy_life, container, false);
        initData(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.dailylife_recycler);

        return view;
    }

    public void initData(final View view) {
        HttpModel.bulid().getDailyLife(new Observer<DailyLifeBean>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(DailyLifeBean dailyLifeBean) {
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                adapter = new DailyLifeRecyclerAdapter(dailyLifeBean.getData(), view.getContext());
                recyclerView.setAdapter(adapter);
            }
        });
    }

}
