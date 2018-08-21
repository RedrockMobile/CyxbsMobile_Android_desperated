package com.mredrock.cyxbs.network.kt

import com.mredrock.cyxbs.config.Const
import com.mredrock.cyxbs.model.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by mingyang.zeng on 2017/8/29.
 */

interface RedrockApiServiceKt {

    @FormUrlEncoded
    @POST(Const.API_VERIFY)
    fun verify(@Field("stuNum") stuNum: String,
               @Field("idNum") idNum: String)
            : io.reactivex.Observable<User.UserWrapper>

    @FormUrlEncoded
    @POST(Const.API_GET_INFO)
    fun getPersonInfo(@Field("stuNum") stuNum: String,
                      @Field("idNum") idNum: String)
            : io.reactivex.Observable<User.UserWrapper>
}
