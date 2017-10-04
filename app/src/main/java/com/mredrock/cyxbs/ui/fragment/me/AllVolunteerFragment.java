package com.mredrock.cyxbs.ui.fragment.me;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;
import com.mredrock.cyxbs.ui.adapter.me.VolunteerRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by glossimar on 2017/10/3.
 */

public class AllVolunteerFragment extends Fragment {
    private View view;
    private TextView holeTime;
    private RecyclerView recyclerView;

    private Context context;
    private String allHour;
    private List<String> yearList;
    private List<List<VolunteerTime.DataBean.RecordBean>> recordBeanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_volunteer_time,container,false);
        holeTime = (TextView)view.findViewById(R.id.volunteer_time_number);
        recyclerView = (RecyclerView) view.findViewById(R.id.volunteer_time_recycler);
        initData();
        return view;
    }

    private void initData() {
        holeTime.setText(allHour);
        VolunteerRecyclerAdapter adapter = new VolunteerRecyclerAdapter(null, context, getYearList(), getRecordBeanList());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public List<List<VolunteerTime.DataBean.RecordBean>> getRecordBeanList() {
        return recordBeanList;
    }

    public void setRecordBeanList(List<List<VolunteerTime.DataBean.RecordBean>> recordBeanList) {
        this.recordBeanList = recordBeanList;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getAllHour() {
        return allHour;
    }

    public void setAllHour(String allHour) {
        this.allHour = allHour;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }
}
