package com.mredrock.freshmanspecial.model;


import android.util.Log;

import com.mredrock.freshmanspecial.beans.CafeteriaBean;
import com.mredrock.freshmanspecial.beans.CampusBean;
import com.mredrock.freshmanspecial.beans.CuisineBean;
import com.mredrock.freshmanspecial.beans.DailyLifeBean;
import com.mredrock.freshmanspecial.beans.DormitoryBean;
import com.mredrock.freshmanspecial.beans.EmploymentData;
import com.mredrock.freshmanspecial.beans.FengcaiBeans.JunxunpicBeans;
import com.mredrock.freshmanspecial.beans.FengcaiBeans.JunxunvideoBeans;
import com.mredrock.freshmanspecial.beans.HttpResult;
import com.mredrock.freshmanspecial.beans.MienBeans.BeautyBean;
import com.mredrock.freshmanspecial.beans.MienBeans.OriginalBean;
import com.mredrock.freshmanspecial.beans.MienBeans.StudentsBean;
import com.mredrock.freshmanspecial.beans.MienBeans.TeacherBean;
import com.mredrock.freshmanspecial.beans.QQGroupsBean;
import com.mredrock.freshmanspecial.beans.ShujuBeans.FailBean;
import com.mredrock.freshmanspecial.beans.ShujuBeans.SexBean;
import com.mredrock.freshmanspecial.beans.ShujuBeans.WorkBean;
import com.mredrock.freshmanspecial.beans.SurroundingBeautyBean;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zxzhu on 2017/8/6.
 */

public class HttpModel {

    public static final String URL = "http://hongyan.cqupt.edu.cn/welcome/2017/api/";
    private static final int DEFAULT_TIMEOUT = 5;
    private static Retrofit retrofit;
    private static Services service;
    //private Context context;


    /**
     * 获取单例
     *
     * @return
     */
    private HttpModel() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(URL)
                .build();

        service = retrofit.create(Services.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpModel INSTANCE = new HttpModel();
    }

    //获取单例
    public static HttpModel bulid() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 获取男女比例
     *
     * @param subscriber
     */
    public void getSex(Subscriber<SexBean> subscriber) {
        Log.e("ttt", "getSex");
        service.getSex("SexRatio")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取就业率
     */
    public Observable<WorkBean> getWork() {
        return service.getWork("WorkRatio").
                subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取挂科率
     */
    public Observable<FailBean> getFail() {
        return service.getFail("FailRatio")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取军训视频
     *
     * @return video
     */
    public Observable<JunxunvideoBeans> getJunxunvideo() {
        return service.getJunxunvideo("MilitaryTrainingVideo")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取军训图片
     *
     * @return pic
     */
    public Observable<JunxunpicBeans> getJunxunpic() {
        return service.getJunxunpic("MilitaryTrainingPhoto")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取QQ群
     *
     * @param subscriber
     */
    public void getQQGroups(Subscriber<QQGroupsBean> subscriber) {
        service.getQQGroups("QQGroup")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取优秀教师
     *
     * @param subscriber
     */
    public void getTeachers(Subscriber<TeacherBean> subscriber) {
        service.getTeachers("excellentTech")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取优秀学生
     *
     * @param subscriber
     */
    public void getStudents(Subscriber<StudentsBean> subscriber) {
        service.getStudents("excellentStu")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取美在重邮
     *
     * @param subscriber
     */
    public void getBeauties(Subscriber<BeautyBean> subscriber) {
        service.getBeauties("beautyInCQUPT")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取原创重邮
     *
     * @param subscriber
     */
    public void getBOriginal(Subscriber<OriginalBean> subscriber) {
        service.getOriginal("natureCQUPT")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCafeteria(Subscriber<CafeteriaBean> subscriber) {
        service.getCafeteriaBean("Canteen")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSchoolBuildings(Subscriber<CampusBean> subscriber) {
        service.getSchoolBuildings("SchoolBuildings")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDailyLife(Subscriber<DailyLifeBean> subscriber) {
        service.getDailyLife("LifeInNear")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDormitory(Subscriber<DormitoryBean> subscriber) {
        service.getDormitory("Dormitory")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCuisine(Subscriber<CuisineBean> subscriber) {
        service.getCuisine("Cate")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getSurroundingBeauty(Subscriber<SurroundingBeautyBean> subscriber) {
        service.getSurroundingBeauty("BeautyInNear")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getEmploymentData(Subscriber<ArrayList<EmploymentData>> subscriber) {
        service.getEmploymentData("DataOfJob")
                .map(new Func1<HttpResult<ArrayList<EmploymentData>>, ArrayList<EmploymentData>>() {
                    @Override
                    public ArrayList<EmploymentData> call
                            (HttpResult<ArrayList<EmploymentData>> result) {
                        return result.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}