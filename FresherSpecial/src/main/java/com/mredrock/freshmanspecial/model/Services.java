package com.mredrock.freshmanspecial.model;

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

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zxzhu on 2017/8/6.
 */

public interface Services {

    //http://www.yangruixin.com/test/apiRatio.php
    @FormUrlEncoded
    @POST("apiRatio.php")
    Observable<SexBean> getSex(@Field("RequestType") String requestType);

    @FormUrlEncoded
    @POST("apiRatio.php")
    Observable<WorkBean> getWork(@Field("RequestType") String requestType);

    @FormUrlEncoded
    @POST("apiRatio.php")
    Observable<QQGroupsBean> getQQGroups(@Field("RequestType") String requestType);

    @FormUrlEncoded
    @POST("apiRatio.php")
    Observable<FailBean> getFail(@Field("RequestType") String requestType);

    @FormUrlEncoded
    @POST("apiRatio.php")
    Observable<HttpResult<ArrayList<EmploymentData>>> getEmploymentData(
            @Field("RequestType") String type);

    @GET("apiForGuide.php")
    Observable<JunxunpicBeans> getJunxunpic(@Query("RequestType") String requestType);

    @GET("apiForGuide.php")
    Observable<JunxunvideoBeans> getJunxunvideo(@Query("RequestType") String requestType);

    @GET("apiForGuide.php")
    Observable<CafeteriaBean> getCafeteriaBean(@Query("RequestType") String requestType);

    @GET("apiForGuide.php")
    Observable<CampusBean> getSchoolBuildings(@Query("RequestType") String requestType);

    @GET("apiForGuide.php")
    Observable<DailyLifeBean> getDailyLife(@Query("RequestType") String requestType);

    @GET("apiForGuide.php")
    Observable<DormitoryBean> getDormitory(@Query("RequestType") String requestType);

    @GET("apiForGuide.php")
    Observable<CuisineBean> getCuisine(@Query("RequestType") String requestType);

    @GET("apiForGuide.php")
    Observable<SurroundingBeautyBean> getSurroundingBeauty(@Query("RequestType") String requestType);

    @GET("apiForText.php")
    Observable<TeacherBean> getTeachers(@Query("RequestType") String requestType);

    @GET("apiForText.php")
    Observable<StudentsBean> getStudents(@Query("RequestType") String requestType);

    @GET("apiForText.php")
    Observable<BeautyBean> getBeauties(@Query("RequestType") String requestType);

    @GET("apiForText.php")
    Observable<OriginalBean> getOriginal(@Query("RequestType") String requestType);
}