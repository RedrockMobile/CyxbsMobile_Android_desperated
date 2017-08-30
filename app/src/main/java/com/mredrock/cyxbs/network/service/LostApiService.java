package com.mredrock.cyxbs.network.service;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.lost.Lost;
import com.mredrock.cyxbs.model.lost.LostDetail;
import com.mredrock.cyxbs.model.lost.LostStatus;
import com.mredrock.cyxbs.model.lost.LostWrapper;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import io.reactivex.Observable;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public interface LostApiService {

    @GET(Const.END_POINT_LOST + Const.API_LOST_LIST)
    Observable<LostWrapper<List<Lost>>> getLostList(@Path(Const.PATH_THEME) String theme,
                                              @Path(Const.PATH_CATEGORY) String category,
                                              @Path(Const.PATH_PAGE) int page);

    @GET(Const.END_POINT_LOST + Const.API_LOST_DETAIL)
    Observable<LostDetail> getLostDetial(@Path(Const.PATH_PRODUCT) int id);

    @FormUrlEncoded
    @POST(Const.END_POINT_LOST + Const.API_LOST_CREATE)
    Observable<LostStatus> create(@Field("stu_num") String stuNum,
                                  @Field("idNum") String idNum,
                                  @Field("property") String theme,
                                  @Field("category") String category,
                                  @Field("detail") String description,
                                  @Field("pickTime") String time,
                                  @Field("place") String place,
                                  @Field("phone") String phone,
                                  @Field("qq") String qq);

}
