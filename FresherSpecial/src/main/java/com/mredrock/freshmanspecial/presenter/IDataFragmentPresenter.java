package com.mredrock.freshmanspecial.presenter;

import com.mredrock.freshmanspecial.beans.ShujuBeans.SexBean;
import com.mredrock.freshmanspecial.beans.ShujuBeans.WorkBean;
import com.mredrock.freshmanspecial.units.ChartData;

import java.util.List;

/**
 * Created by zia on 17-8-5.
 */

public interface IDataFragmentPresenter {
    void setSexRateCollegeList();//设置本地男女比例的专业集合
    void setWorkRateCollegeList();//设置本地男女比例的专业集合
    void setSexRateDataList(SexBean.DataBean bean);//网络加载男女比例信息
    void setFailCollegeList();//设置本地挂科率的专业集合
    void setJobRateDataList(WorkBean.DataBean bean);//通过bean设置图表信息
    void showPickerView(List<String> collegeList, DataFragmentPresenter.OnPickerViewChoosed onPickerViewChoosed);//弹出pickerView
    void disMissPickerView();//关闭pickerView
    void loadJobRateData(String college, DataFragmentPresenter.OnDataLoaded onDataLoaded);//从网络加载就业率信息
    void loadFailMajorList(String college, DataFragmentPresenter.OnDataLoaded onDataLoaded);//从网络加载挂科率信息
    void loadFailData(String major, DataFragmentPresenter.OnDataLoaded onDataLoaded);//本地筛选挂科信息
    void runChart(List<ChartData> dataList);//开启动画图表
    void setEmptySexRateDate();
    void setEmptyFailData();
    void setEmptyWorkRate();
}
