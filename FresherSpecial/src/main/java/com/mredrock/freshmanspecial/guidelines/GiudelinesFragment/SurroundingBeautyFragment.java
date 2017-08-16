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
import com.mredrock.freshmanspecial.beans.GuidelinesHorizontalBean;
import com.mredrock.freshmanspecial.beans.SurroundingBeautyBean;
import com.mredrock.freshmanspecial.guidelines.Adapter.BeautyRecyclerAdapter;
import com.mredrock.freshmanspecial.model.HttpModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Glossimar on 2017/8/3.
 * 专题 ： 邮子攻略
 * 界面 ： 邮子攻略-周边美景
 */

public class SurroundingBeautyFragment extends Fragment {
    private List<GuidelinesHorizontalBean> shopBeanList;
    private RecyclerView recyclerView;
    private BeautyRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_2017_fragment_surrounding, container, false);
        initData(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.surrounding_recycler);

        return view;
    }

    public void initData(final View view) {
        HttpModel.bulid().getSurroundingBeauty(new Subscriber<SurroundingBeautyBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(SurroundingBeautyBean surroundingBeautyBean) {
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                adapter = new BeautyRecyclerAdapter(surroundingBeautyBean.getData(), view.getContext());
                recyclerView.setAdapter(adapter);
            }
        });
    }


}
