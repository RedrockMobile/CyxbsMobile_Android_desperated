package com.mredrock.freshmanspecial.presenter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.beans.ShujuBeans.FailBean;
import com.mredrock.freshmanspecial.beans.ShujuBeans.SexBean;
import com.mredrock.freshmanspecial.beans.ShujuBeans.WorkBean;
import com.mredrock.freshmanspecial.model.DataModel;
import com.mredrock.freshmanspecial.model.HttpModel;
import com.mredrock.freshmanspecial.units.ChartData;
import com.mredrock.freshmanspecial.view.dataFragments.IDataFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by zia on 17-8-5.
 */

public class DataFragmentPresenter implements IDataFragmentPresenter {

    private IDataFragment fragment;
    private DataModel model;
    private OptionsPickerView optionsPickerView;
    private List<FailBean.DataBean> failDataBeanList = new ArrayList<FailBean.DataBean>();
    private final static String TAG = "dataFragmentPresenter";

    public DataFragmentPresenter(IDataFragment fragment) {
        this.fragment = fragment;
        model = new DataModel();
    }


    /**
     * 设置学院名称集合，fragment必须实现IDataFragment接口
     */
    @Override
    public void setSexRateCollegeList() {
        fragment.getCollegeList().clear();
        fragment.getCollegeList().addAll(model.getSexCollegeList());
    }

    @Override
    public void setWorkRateCollegeList() {
        fragment.getCollegeList().clear();
        fragment.getCollegeList().addAll(model.getWorkRateCollegeList());
    }


    @Override
    public void setSexRateDataList(SexBean.DataBean bean) {
        fragment.getDataList().clear();
        fragment.getDataList().addAll(model.getSexRateDataList(bean));
    }

    @Override
    public void setFailCollegeList() {
        fragment.getCollegeList().clear();
        fragment.getCollegeList().addAll(model.getMostDifficultCollege());
    }

    @Override
    public void setJobRateDataList(WorkBean.DataBean bean) {
        fragment.getDataList().clear();
        if (model.getJobRateDataList(bean) != null) {
            fragment.getDataList().addAll(model.getJobRateDataList(bean));
        }
    }


    /**
     * 一个回调接口，当pickerView
     */
    public interface OnPickerViewChoosed {
        void getString(String data);
    }

    /**
     * 生成PickerView并在底部显示
     */
    @Override
    public void showPickerView(final List<String> collegeList, final OnPickerViewChoosed onPickerViewChoosed) {
        optionsPickerView = new OptionsPickerView.Builder(fragment.getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //选择回调接口
                onPickerViewChoosed.getString(collegeList.get(options1));
                Log.d("test", collegeList.get(options1));
            }
        })
                .setLayoutRes(R.layout.special_2017_options_picker_view, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView textView = v.findViewById(R.id.tv_finish);
                        textView.setOnClickListener(view -> {
                            optionsPickerView.returnData(view);
                            disMissPickerView();
                        });
                    }
                })
                .setTextColorCenter(Color.parseColor("#81C0FE"))
                .setBgColor(Color.parseColor("#FFFFFF"))
                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .setSubmitColor(Color.parseColor("#81C0FE"))
                .build();
        optionsPickerView.setPicker(collegeList);
        optionsPickerView.show();

    }

    @Override
    public void disMissPickerView() {
        if (optionsPickerView != null) {
            optionsPickerView.dismiss();
        }
    }

    @Override
    public void runChart(List<ChartData> dataList) {
        if (fragment.getChart() == null) return;
        fragment.getChart().setData(dataList);
        //fragment.getChart().setSpace(ScreenUnit.dip2px(fragment.getActivity(),28));
        fragment.getChart().setSpeed(2);
        fragment.getChart().run();
    }

    @Override
    public void setEmptySexRateDate() {
        fragment.getDataList().clear();
        SexBean.DataBean bean = new SexBean.DataBean();
        bean.setMenRatio("0");
        bean.setWomenRatio("0");
        fragment.getDataList().addAll(model.getSexRateDataList(bean));
    }

    @Override
    public void setEmptyFailData() {
        fragment.getDataList().clear();
        FailBean.DataBean bean = new FailBean.DataBean();
        bean.setCourse("");
        bean.setRatio("0");
        bean.setMajor("");
        bean.setCollege("");
        List<FailBean.DataBean> list = new ArrayList<>();
        list.add(bean);
        list.add(bean);
        list.add(bean);
        fragment.getDataList().addAll(model.getMostDifficultDataList(list));
    }

    @Override
    public void setEmptyWorkRate() {
        fragment.getDataList().clear();
        WorkBean.DataBean bean = new WorkBean.DataBean();
        bean.setRatio("0");
        fragment.getDataList().addAll(model.getJobRateDataList(bean));
    }

    /**
     * 回调
     */
    public interface OnDataLoaded {
        void finish(String msg);
    }

    @Override
    public void loadJobRateData(final String college, final OnDataLoaded onDataLoaded) {
        HttpModel.bulid().getWork()
                .flatMap(workBean -> Observable.fromIterable(workBean.getData()))
                .subscribe(new Observer<WorkBean.DataBean>() {
                    @Override
                    public void onComplete() {
                        onDataLoaded.finish(college);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        fragment.toast("请检查网络！");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WorkBean.DataBean dataBean) {
                        if (dataBean.getCollege().equals(college)) {
                            Log.d("Job", dataBean.getCollege());
                            setJobRateDataList(dataBean);
                        }
                    }
                });
    }

    @Override
    public void loadFailMajorList(final String college, final OnDataLoaded onDataLoaded) {
        failDataBeanList.clear();
        HttpModel.bulid().getFail()
                .flatMap(failBean -> Observable.fromIterable(failBean.getData()))
                .subscribe(new Observer<FailBean.DataBean>() {
                    @Override
                    public void onComplete() {
                        fragment.getMajorList().clear();
                        for (FailBean.DataBean d : failDataBeanList) {
                            fragment.getMajorList().add(d.getMajor());
                        }
                        //清除重复元素
                        for (int i = 0; i < fragment.getMajorList().size() - 1; i++) {
                            for (int j = fragment.getMajorList().size() - 1; j > i; j--) {
                                if (fragment.getMajorList().get(j).equals(fragment.getMajorList().get(i))) {
                                    fragment.getMajorList().remove(j);
                                }
                            }
                        }
                        onDataLoaded.finish("Load OK");
                    }

                    @Override
                    public void onError(Throwable e) {
                        fragment.toast("获取信息失败！");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FailBean.DataBean dataBean) {
                        if (dataBean.getCollege().equals(college)) {
                            failDataBeanList.add(dataBean);
                            Log.d(TAG, dataBean.getCourse());
                        }
                    }
                });
    }

    @Override
    public void loadFailData(final String major, OnDataLoaded onDataLoaded) {
        if (failDataBeanList == null) return;
        //挑选该学院所有major，交给model排序并返回chartData
        List<FailBean.DataBean> majorBeanList = new ArrayList<FailBean.DataBean>();
        for (FailBean.DataBean bean : failDataBeanList) {
            if (bean.getMajor().equals(major)) {
                majorBeanList.add(bean);
                Log.d(TAG, bean.getRatio());
            }
        }
        fragment.getDataList().clear();
        fragment.getDataList().addAll(model.getMostDifficultDataList(majorBeanList));
        onDataLoaded.finish("");
    }
}
