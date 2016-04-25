package com.mredrock.cyxbsmobile.network.service;

import com.mredrock.cyxbsmobile.model.RestaurantComment;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.RestaurantDetail;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.model.EatWhat;
import com.mredrock.cyxbsmobile.model.MovieResult;
import com.mredrock.cyxbsmobile.model.Restaurant;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.model.Subject;

import java.sql.Wrapper;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cc on 16/1/20.
 */
public interface RedrockApiService {

    String MOVIE_URL = "https://api.douban.com/v2/movie/top250";

    @GET
    Observable<MovieResult<List<Subject>>> getTopMovie(@Url String url, @Query("start") int start, @Query("count") int count);

    @FormUrlEncoded
    @Headers("API_APP: android")
    @POST(Const.API_PERSON_SCHEDULE)
    Observable<Course.CourseWrapper> getCourse(@Field("stuNum") String stuNum, @Field("idNum") String idNum, @Field("week") String week);

    @GET(Const.API_EAT_WHAT)
    Observable<RedrockApiWrapper<EatWhat>> getEatWhat();

    @FormUrlEncoded
    @POST(Const.API_AROUND_FOOD_RESTAURANTS)
    Observable<RedrockApiWrapper<List<Restaurant>>> getRestaurantList(@Field("pid") String page);

    @FormUrlEncoded
    @POST(Const.API_RESTAURANT_DETAIL)
    Observable<RedrockApiWrapper<RestaurantDetail>> getRestaurantDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST(Const.API_RESTAURANT_COMMENTS)
    Observable<RedrockApiWrapper<List<RestaurantComment>>> getRestaurantComments(@Field("shop_id") String id, @Field("pid") String page);

    @FormUrlEncoded
    @POST(Const.API_RESTAURANT_SEND_COMMENT)
    Observable<RedrockApiWrapper<Object>> sendRestaurantComment(@Field("shop_id") String id, @Field("user_number") String userNumber, @Field("user_password") String userPassword,@Field("comment_content") String commentContent,@Field("comment_author_name") String commentAuthoName);
}
