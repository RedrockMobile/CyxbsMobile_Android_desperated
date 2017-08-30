package com.mredrock.freshmanspecial.view.MienFragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.beans.MienBeans.BeautyBean;
import com.mredrock.freshmanspecial.model.HttpModel;
import com.mredrock.freshmanspecial.units.MyRecyclerAdapter;
import com.mredrock.freshmanspecial.units.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by zxzhu on 2017/8/4.
 */

public class BeautyFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private List<BeautyBean.DataBean> list;

    @Override
    protected void initData() {
        recyclerView = $(R.id.list_beauty);
        list = new ArrayList<>();
        manager = new GridLayoutManager(getActivity(), 1);
        setData();
    }

    private void setData() {

        HttpModel.bulid().getBeauties(new Observer<BeautyBean>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(BeautyBean beautyBean) {
                list = beautyBean.getData();
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(new MyRecyclerAdapter(getActivity(), list, MyRecyclerAdapter.BEAUTY));
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.special_2017_fragment_beauty;
    }
}
