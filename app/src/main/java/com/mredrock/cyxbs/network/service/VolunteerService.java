package com.mredrock.cyxbs.network.service;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.VolunteerTime;

import rx.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by glossimar on 2017/9/27.
 */

public interface VolunteerService {

    @FormUrlEncoded
    @POST(Const.API_VOLUNTEER_LOGIN)
    Observable<VolunteerTime> getVolunteerUseLogin(@Field("account") String account, @Field("password") String password);

    @FormUrlEncoded
    @POST(Const.API_VOLUNTEER_UID)
    Observable<VolunteerTime> getVolunteerUseUid(@Field("uid") String uid);
}
