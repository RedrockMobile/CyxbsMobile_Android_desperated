package com.mredrock.freshmanspecial.view.dataFragments;


import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.ChartData;
import com.mredrock.freshmanspecial.units.CircleChart;
import com.mredrock.freshmanspecial.units.SmallCircle;
import com.mredrock.freshmanspecial.units.base.BaseFragment;
import com.mredrock.freshmanspecial.presenter.DataFragmentPresenter;
import com.mredrock.freshmanspecial.presenter.IDataFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 最难科目数据组装
 */
public class MostDifficultFragment extends BaseFragment implements IDataFragment {

    private Button button,majorButton;
    private List<String> collegeList = new ArrayList<>();
    private List<String> majorList = new ArrayList<>();
    private List<ChartData> dataList = new ArrayList<>();
    private CircleChart circleChart;
    private SmallCircle smallCircle;
    private IDataFragmentPresenter presenter;


    @Override
    protected void initData() {
        presenter = new DataFragmentPresenter(this);
        button = $(R.id.mostDifficult_button);
        majorButton = $(R.id.mostDifficult_button_major);
        circleChart = $(R.id.mostDifficult_chart);
        smallCircle = $(R.id.mostDifficult_circle);
        presenter.setEmptyFailData();
        presenter.runChart(dataList);
        initSmallCircle();
        //获取所有学院信息，加载到第一个按钮上
        presenter.setFailCollegeList();
        majorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "请先选择学院", Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将获取到的信息以PickerView呈现出来
                presenter.showPickerView(collegeList, new DataFragmentPresenter.OnPickerViewChoosed() {
                    @Override
                    public void getString(String data) {
                        //设置按钮文字
                        button.setText(data);
                        //将选择的学院进行网络请求，获取该学院专业，同时刷新到majorList保存
                        presenter.loadFailMajorList(data, new DataFragmentPresenter.OnDataLoaded() {
                            @Override
                            public void finish(String msg) {
                                //关闭之前的pickerView
                                //presenter.disMissPickerView();
                                //将majorList的数据呈现在新的PickerView上
                                showMajorPickerView();
                                //同时给按钮设置监听
                                majorButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showMajorPickerView();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void showMajorPickerView(){
        presenter.showPickerView(majorList, new DataFragmentPresenter.OnPickerViewChoosed() {
            @Override
            public void getString(String data) {
                //设置按钮文字
                majorButton.setText(data);
                //将选取的学院给presenter处理，得到circleChart数据
                presenter.loadFailData(data, new DataFragmentPresenter.OnDataLoaded() {
                    @Override
                    public void finish(String msg) {
                        presenter.runChart(dataList);
                        initSmallCircle();
                    }
                });
            }
        });
    }

    private void initSmallCircle(){
        if(dataList.size() == 0) return;
        List<String> texts = new ArrayList<String>();
        List<Integer> colors = new ArrayList<Integer>();
        List<Integer> shadows = new ArrayList<Integer>();
        for (ChartData d : dataList) {
            texts.add(d.getText());
            colors.add(d.getColor());
            shadows.add(d.getStrokeColor());
        }
        smallCircle.setTexts(texts);
        smallCircle.setColors(colors);
        smallCircle.setShadows(shadows);
        smallCircle.draw();
    }

    @Override
    protected int getResourceId() {
        return R.layout.special_2017_fragment_most_difficult;
    }

    @Override
    public List<String> getCollegeList() {
        return collegeList;
    }

    @Override
    public List<ChartData> getDataList() {
        return dataList;
    }

    @Override
    public List<String> getMajorList() {
        return majorList;
    }

    @Override
    public CircleChart getChart() {
        return circleChart;
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
