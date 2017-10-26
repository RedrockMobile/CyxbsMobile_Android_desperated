package com.mredrock.cyxbs.ui.fragment.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
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
 * Created by glossimar on 2017/10/1.
 */

public class FirstVolunteerTimeFragment extends Fragment{
    private View view;
    private TextView holeTime;
    private RecyclerView recyclerView;

    private Context context;

    private String year;
    private List<String> yearList;
    private List<VolunteerTime.DataBean.RecordBean> recordBeanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_volunteer_time,container,false);
        holeTime = (TextView)view.findViewById(R.id.volunteer_time_number);
        recyclerView = (RecyclerView) view.findViewById(R.id.volunteer_time_recycler);
        yearList = new ArrayList<>();
        initData();
        return view;
    }

    private void initData() {
        int holehour = 0;
        for (int i = 0; i < recordBeanList.size(); i ++) {
            int nowHour = (int) Double.parseDouble(recordBeanList.get(i).getHours());
            holehour = holehour + nowHour;
        }
        String holeHourString = holehour + "";
        holeTime.setText(holeHourString);
        yearList.add(year);
        VolunteerRecyclerAdapter adapter = new VolunteerRecyclerAdapter(recordBeanList, context, yearList, null);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public List<VolunteerTime.DataBean.RecordBean> getRecordBeanList() {
        return recordBeanList;
    }

    public void setRecordBeanList(List<VolunteerTime.DataBean.RecordBean> recordBeanList) {
        this.recordBeanList = recordBeanList;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
