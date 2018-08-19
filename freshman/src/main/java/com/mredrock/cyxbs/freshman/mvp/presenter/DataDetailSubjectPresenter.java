package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.bean.SubjectProportion;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.DataDetailSubjectContract;
import com.mredrock.cyxbs.freshman.mvp.model.DataDetailSubjectModel;

/*
 by Cynthia at 2018/8/17
 description : 
 */
public class DataDetailSubjectPresenter extends BasePresenter<DataDetailSubjectContract.IDataDetailSubjectView> {

    private DataDetailSubjectModel model;
    private SubjectProportion subject;

    public DataDetailSubjectPresenter(DataDetailSubjectModel model) {
        this.model = model;
    }

    public void start() {
        model.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                subject = (SubjectProportion) o;
                getView().loadSubjectView(subject);
            }

            @Override
            public void failed(String msg) {
                getView().showError(msg);
            }
        });
    }
}
