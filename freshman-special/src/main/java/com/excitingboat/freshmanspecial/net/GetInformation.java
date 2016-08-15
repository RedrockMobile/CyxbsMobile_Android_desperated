package com.excitingboat.freshmanspecial.net;

import com.excitingboat.freshmanspecial.config.Api;
import com.excitingboat.freshmanspecial.model.bean.Dormitory;
import com.excitingboat.freshmanspecial.model.bean.PlaceWithIntroduction;
import com.excitingboat.freshmanspecial.model.bean.Sight;
import com.excitingboat.freshmanspecial.model.bean.Student;
import com.excitingboat.freshmanspecial.model.bean.SurroundSight;
import com.excitingboat.freshmanspecial.model.bean.Teacher;
import com.excitingboat.freshmanspecial.model.bean.Video;
import com.excitingboat.freshmanspecial.model.bean.Wrapper;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by PinkD on 2016/8/4.
 * Retrofit Network Interface
 */
public interface GetInformation {

    int STUDENT = 0;
    int TEACHER = 1;
    int VIDEO = 2;
    int DORMITORY = 3;
    int SIGHT = 4;
    int DAILY_LIFE = 5;
    int FOOD = 6;
    int CQUPT_SIGHT = 7;


    @FormUrlEncoded
    @POST(Api.STUDENT)
    Observable<Wrapper<Student>> getStudent(@Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST(Api.TEACHER)
    Observable<Wrapper<Teacher>> getTeacher(@Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST(Api.ORIGINAL)
    Observable<Wrapper<Video>> getVideo(@Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST(Api.DORMITORY)
    Observable<Wrapper<Dormitory>> getDormitory(@Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST(Api.SIGHT)
    Observable<Wrapper<SurroundSight>> getSight(@Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST(Api.DAILY_LIFE)
    Observable<Wrapper<PlaceWithIntroduction>> getDaily(@Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST(Api.FOOD)
    Observable<Wrapper<PlaceWithIntroduction>> getFood(@Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST(Api.CQUPT_SIGHT)
    Observable<Wrapper<Sight>> getCQUPTSight(@Field("page") int page, @Field("size") int size);


}
