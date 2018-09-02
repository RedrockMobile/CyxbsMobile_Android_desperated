package com.mredrock.cyxbs.freshman.mvp.contract;

import com.mredrock.cyxbs.freshman.bean.SubjectProportion;

/*
 by Cynthia at 2018/8/17
 description : 
 */
public class DataDetailSubjectContract {
    public interface IDataDetailSubjectModel extends BaseContract.ISomethingModel {
        void setSubject(SubjectProportion subject, LoadCallBack loadCallBack);

        void error(String error, LoadCallBack callBack);
    }

    public interface IDataDetailSubjectView extends BaseContract.ISomethingView {
        void showError(String msg);

        void loadSubjectView(SubjectProportion subjectProportion);
    }
}
