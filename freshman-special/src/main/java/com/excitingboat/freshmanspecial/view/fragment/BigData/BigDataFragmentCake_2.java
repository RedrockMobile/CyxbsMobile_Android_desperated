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
import com.excitingboat.freshmanspecial.view.adapter.BigDataAdapter;
import com.excitingboat.freshmanspecial.view.adapter.MyColorTextAdapter;
import com.excitingboat.yellowcake.ColorTextListView;
import com.excitingboat.yellowcake.Yellowcake;

/**
 * Created by PinkD on 2016/8/4.
 * BigDataFragmentCake_1
 */
public class BigDataFragmentCake_2 extends Fragment {
    private static final String TAG = "BigDataFragmentCake_1";
    private BigDataAdapter bigDataAdapter;
    private Spinner spinner1;
    private Yellowcake yellowcake;
    private MyColorTextAdapter myColorTextAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_freshman_special__fragment_bd_cake, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        spinner1 = (Spinner) view.findViewById(R.id.spinner_1);
        view.findViewById(R.id.spinner_2).setVisibility(View.GONE);
        yellowcake = (Yellowcake) view.findViewById(R.id.cake);


        ColorTextListView colorTextListView = (ColorTextListView) view.findViewById(R.id.list);
        myColorTextAdapter = new MyColorTextAdapter();
        colorTextListView.setMax(2);
        colorTextListView.setAdapter(myColorTextAdapter);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.project_freshman_special__spinner, bigDataAdapter.getSchool());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉框显示值的样式
        arrayAdapter.notifyDataSetChanged();
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: position:" + position);
                if (position != 0) {
                    if (yellowcake.getVisibility() != View.VISIBLE) {
                        yellowcake.setVisibility(View.VISIBLE);
                    }
                    myColorTextAdapter.clear();
                    bigDataAdapter.setData(yellowcake, myColorTextAdapter, position - 1);
                } else {
                    resetCake();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public BigDataAdapter getBigDataAdapter() {
        return bigDataAdapter;
    }

    public void setBigDataAdapter(BigDataAdapter bigDataAdapter) {
        this.bigDataAdapter = bigDataAdapter;
    }

    private void resetCake() {
        yellowcake.setVisibility(View.GONE);
        myColorTextAdapter.clear();
    }

}
