package com.excitingboat.freshmanspecial.view.fragment.BigData;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.config.BigData;
import com.excitingboat.freshmanspecial.view.adapter.BigDataAdapter;
import com.excitingboat.freshmanspecial.view.adapter.BigDataAdapter_1;
import com.excitingboat.freshmanspecial.view.adapter.MyColorTextAdapter;
import com.excitingboat.yellowcake.ColorTextListView;
import com.excitingboat.yellowcake.Yellowcake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by PinkD on 2016/8/4.
 * BigDataFragmentCake_1
 */
public class BigDataFragmentCake_1 extends Fragment {
    private static final String TAG = "BigDataFragmentCake_1";
    private BigDataAdapter_1 bigDataAdapter;
    private List<String> major;
    private Spinner spinner1;
    private Spinner spinner2;
    private Yellowcake yellowcake;
    private ArrayAdapter<String> arrayAdapter2;
    private MyColorTextAdapter myColorTextAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_freshman_special__fragment_bd_cake, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        spinner1 = (Spinner) view.findViewById(R.id.spinner_1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner_2);
        yellowcake = (Yellowcake) view.findViewById(R.id.cake);


        ColorTextListView colorTextListView = (ColorTextListView) view.findViewById(R.id.list);
        myColorTextAdapter = new MyColorTextAdapter();
        colorTextListView.setMax(2);
        colorTextListView.setAdapter(myColorTextAdapter);
        major = new ArrayList<>();

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getContext(), R.layout.project_freshman_special__spinner, bigDataAdapter.getSchool());
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉框显示值的样式
        arrayAdapter2 = new ArrayAdapter<>(getContext(), R.layout.project_freshman_special__spinner, major);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉框显示值的样式
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: position:" + position);
                resetMajor();
                if (position == 0) {
                    resetCake();
                    return;
                }
                Collections.addAll(major, BigData.MAJOR[position - 1]);
                bigDataAdapter.setSchoolPosition(position - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner1.getSelectedItemId() > 0 && position > 0) {
                    myColorTextAdapter.clear();
                    bigDataAdapter.setData(yellowcake, myColorTextAdapter, position - 1);
                    if (yellowcake.getVisibility() != View.VISIBLE) {
                        yellowcake.setVisibility(View.VISIBLE);
                    }
                } else {
                    resetCake();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        resetMajor();
    }


    private void resetMajor() {
        major.clear();
        major.add("请选择专业");
        arrayAdapter2.notifyDataSetChanged();
        spinner2.setSelection(0);
        resetCake();
    }

    private void resetCake() {
        yellowcake.setVisibility(View.GONE);
        myColorTextAdapter.clear();
    }

    public BigDataAdapter getBigDataAdapter() {
        return bigDataAdapter;
    }

    public void setBigDataAdapter(BigDataAdapter_1 bigDataAdapter) {
        this.bigDataAdapter = bigDataAdapter;
    }
}
