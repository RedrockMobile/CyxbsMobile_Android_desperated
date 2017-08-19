package com.mredrock.freshmanspecial.view.MienFragments;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.mredrock.freshmanspecial.beans.MienBeans.OriginalBean;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.MyRecyclerAdapter;
import com.mredrock.freshmanspecial.units.base.BaseFragment;
import com.mredrock.freshmanspecial.model.HttpModel;

import rx.Subscriber;

/**
 * Created by zxzhu on 2017/8/4.
 */

public class OriginalFragment extends BaseFragment {
    private RecyclerView recyclerView;
    @Override
    protected void initData() {
        recyclerView = $(R.id.list_original);
        setRecyclerView();
    }

    private void setRecyclerView(){
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        HttpModel.bulid().getBOriginal(new Subscriber<OriginalBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(OriginalBean originalBean) {
                originalBean.getData().get(0).setTime("03:45");
                originalBean.getData().get(1).setTime("04:08");
                originalBean.getData().get(2).setTime("12:44");
                originalBean.getData().get(3).setTime("12:22");
                originalBean.getData().get(4).setTime("12:22");
                originalBean.getData().get(5).setTime("08:53");
                originalBean.getData().get(6).setTime("20:48");
                originalBean.getData().get(7).setTime("02:50");
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new MyRecyclerAdapter(getActivity(),originalBean.getData(),MyRecyclerAdapter.ORIGINAL));
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.special_2017_fragment_original;
    }

}
