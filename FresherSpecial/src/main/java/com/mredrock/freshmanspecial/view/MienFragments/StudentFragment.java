package com.mredrock.freshmanspecial.view.MienFragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.beans.MienBeans.StudentsBean;
import com.mredrock.freshmanspecial.model.HttpModel;
import com.mredrock.freshmanspecial.units.MyRecyclerAdapter;
import com.mredrock.freshmanspecial.units.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by zxzhu on 2017/8/8.
 */

public class StudentFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private List<StudentsBean.DataBean> list;

    @Override
    protected void initData() {
        recyclerView = $(R.id.list_student);
        manager = new GridLayoutManager(getActivity(), 1);
        list = new ArrayList<>();
        setData();

    }

    private void setData() {
        HttpModel.bulid().getStudents(new Observer<StudentsBean>() {
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
            public void onNext(StudentsBean studentsBean) {
                list = studentsBean.getData();
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(new MyRecyclerAdapter(getActivity(), list, MyRecyclerAdapter.STUDENT));
            }
        });

    }

    @Override
    protected int getResourceId() {
        return R.layout.special_2017_fragment_student;
    }
}
