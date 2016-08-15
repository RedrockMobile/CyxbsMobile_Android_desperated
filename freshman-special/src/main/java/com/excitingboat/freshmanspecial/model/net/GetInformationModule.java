package com.excitingboat.freshmanspecial.model.net;

import com.excitingboat.freshmanspecial.config.Config;
import com.excitingboat.freshmanspecial.model.bean.Wrapper;
import com.excitingboat.freshmanspecial.net.GetInformation;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PinkD on 2016/8/4.
 * getInformation From Server
 */
public class GetInformationModule {
    public void getInformation(int[] param, int which, Subscriber<Wrapper> subscriber) {
        GetInformation getInformation = SingleRetrofit.getInstance().getRetrofit().create(GetInformation.class);
        switch (which) {
            case GetInformation.STUDENT:
                getInformation.getStudent(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case GetInformation.TEACHER:
                getInformation.getTeacher(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case GetInformation.VIDEO:
                getInformation.getVideo(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case GetInformation.DORMITORY:
                getInformation.getDormitory(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case GetInformation.SIGHT:
                getInformation.getSight(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case GetInformation.DAILY_LIFE:
                getInformation.getDaily(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case GetInformation.FOOD:
                getInformation.getFood(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
            case GetInformation.CQUPT_SIGHT:
                getInformation.getCQUPTSight(param[Config.PAGE], param[Config.SIZE])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
                break;
        }
    }
}

