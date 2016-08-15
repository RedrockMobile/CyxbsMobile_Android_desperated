package com.excitingboat.freshmanspecial.view.fragment.Style;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.model.bean.Video;
import com.excitingboat.freshmanspecial.presenter.GetInformationPresenter;
import com.excitingboat.freshmanspecial.view.adapter.LinearRecyclerAdapter;
import com.excitingboat.freshmanspecial.view.iview.IGetInformation;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by PinkD on 2016/8/9.
 * VideoListFragment
 */
public class VideoListFragment extends Fragment implements IGetInformation<Video>{

    private static final String TAG = "VideoListFragment";
    private GetInformationPresenter<Video> presenter;
    private RecyclerView recyclerView;
    private LinearRecyclerAdapter linearRecyclerAdapter;
    private int currentPage;

    public void setPresenter(Context context, GetInformationPresenter<Video> presenter) {
        this.presenter = presenter;
        linearRecyclerAdapter = new LinearRecyclerAdapter(context);
        currentPage = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.project_freshman_special__recyclerview, container, false);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            recyclerView.setAdapter(linearRecyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            presenter.getInformation(new int[]{currentPage, 15});
        }catch (Exception e){

        }
    }

    @Override
    public void requestSuccess(List<Video> list) {
        if (list.size() > 0) {
            linearRecyclerAdapter.addAll(list);
            presenter.getInformation(new int[]{++currentPage, 15});
        }
    }

    @Override
    public void requestFail() {
        Toast.makeText(getContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unBind();
    }
}
