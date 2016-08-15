package com.excitingboat.freshmanspecial.presenter;

import android.util.Log;

import com.excitingboat.freshmanspecial.model.bean.Wrapper;
import com.excitingboat.freshmanspecial.model.net.GetInformationModule;
import com.excitingboat.freshmanspecial.view.iview.IGetInformation;

import rx.Subscriber;

/**
 * Created by PinkD on 2016/8/4.
 * 通用Presenter
 */
public class GetInformationPresenter<T> implements BasePresenter {
    public static final boolean DEBUG = false;
    private static final String TAG = "GetInformationPresenter";
    private int total;
    private int which;
    private IGetInformation<T> iGetInformation;
    private GetInformationModule getInformationModule;


    public GetInformationPresenter(IGetInformation<T> iGetInformation, int which) {
        this.iGetInformation = iGetInformation;
        getInformationModule = new GetInformationModule();
        this.which = which;
    }

    public int getWhich() {
        return which;
    }

    public void getInformation(int[] param) {
        getInformationModule.getInformation(param, which, (Subscriber) new MySubscriber());
        if (DEBUG) {
            Log.d(TAG, "request");
        }
    }

    public int getTotal() {
        return total;
    }

    @Override
    public void unBind() {
        iGetInformation = null;
    }

    class MySubscriber extends Subscriber<Wrapper<T>> {

        @Override
        public void onCompleted() {
            if (DEBUG) {
                Log.d(TAG, "onCompleted");
            }
        }

        @Override
        public void onError(Throwable e) {
            iGetInformation.requestFail();

            if (DEBUG) {
                Log.d(TAG, "onError------>" + e.getMessage());
            }
        }

        @Override
        public void onNext(Wrapper<T> wrapper) {
            if (DEBUG) {
                Log.d(TAG, "onNext");
                System.out.println(wrapper.toString());
                System.out.println(wrapper.getData());
            }
            if (iGetInformation != null) {
                iGetInformation.requestSuccess(wrapper.getData());
                total = wrapper.getTotal();
            }
        }
    }
}
