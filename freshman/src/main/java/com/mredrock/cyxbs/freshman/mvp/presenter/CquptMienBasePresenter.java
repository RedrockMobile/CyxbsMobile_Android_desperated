package com.mredrock.cyxbs.freshman.mvp.presenter;

import android.support.v4.app.Fragment;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.MienStu;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.CquptMienBaseContract;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.ui.fragment.CquptMienStuFragment;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class CquptMienBasePresenter extends BasePresenter<CquptMienBaseContract.ICquptMienBaseView> {
    private CquptMienBaseContract.ICquptMienBaseModel model;

    public CquptMienBasePresenter(CquptMienBaseContract.ICquptMienBaseModel model) {
        this.model = model;
    }

    public void start() {
        model.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                MienStu stu = (MienStu) o;
                if (stu != null) {
                    List<Fragment> fragments = new ArrayList<>();
                    List<String> titles = new ArrayList<>();

                    for (int i = 0; i < stu.getArray().size(); i++) {
                        CquptMienStuFragment fragment = new CquptMienStuFragment();
                        fragment.setBean(stu.getArray().get(i));
                        fragments.add(fragment);
                        titles.add(stu.getArray().get(i).getName());
                    }
                    getView().setData(fragments, titles);
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.show(App.getContext().getResources().getString(R.string.freshman_error_soft));
            }
        });

    }


}
