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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
     * @param
     */
    public void getSex(Observer<SexBean> observer) {
        Log.e("ttt", "getSex");
        service.getSex("SexRatio")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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
     * @param observer
     */
    public void getQQGroups(Observer<QQGroupsBean> observer) {
        service.getQQGroups("QQGroup")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取优秀教师
     *
     * @param observer
     */
    public void getTeachers(Observer<TeacherBean> observer) {
        service.getTeachers("excellentTech")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取优秀学生
     *
     * @param observer
     */
    public void getStudents(Observer<StudentsBean> observer) {
        service.getStudents("excellentStu")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(observer);
    }

    /**
     * 获取美在重邮
     *
     * @param observer
     */
    public void getBeauties(Observer<BeautyBean> observer) {
        service.getBeauties("beautyInCQUPT")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取原创重邮
     *
     * @param observer
     */
    public void getBOriginal(Observer<OriginalBean> observer) {
        service.getOriginal("natureCQUPT")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getCafeteria(Observer<CafeteriaBean> observer) {
        service.getCafeteriaBean("Canteen")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSchoolBuildings(Observer<CampusBean> observer) {
        service.getSchoolBuildings("SchoolBuildings")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getDailyLife(Observer<DailyLifeBean> observer) {
        service.getDailyLife("LifeInNear")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getDormitory(Observer<DormitoryBean> observer) {
        service.getDormitory("Dormitory")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getCuisine(Observer<CuisineBean> observer) {
        service.getCuisine("Cate")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSurroundingBeauty(Observer<SurroundingBeautyBean> observer) {
        service.getSurroundingBeauty("BeautyInNear")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getEmploymentData(Observer<ArrayList<EmploymentData>> observer) {
        service.getEmploymentData("DataOfJob")
                .map(HttpResult::getData)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}