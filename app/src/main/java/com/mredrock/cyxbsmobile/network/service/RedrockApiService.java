package com.mredrock.cyxbsmobile.network.service;

import com.mredrock.cyxbsmobile.model.RestaurantComment;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.RestaurantDetail;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.model.EatWhat;
import com.mredrock.cyxbsmobile.model.Restaurant;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.model.Empty;
import com.mredrock.cyxbsmobile.model.Student;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cc on 16/1/20.
 */
public interface RedrockApiService {

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
    Observable<RedrockApiWrapper<Object>> sendRestaurantComment(@Field("shop_id") String id, @Field("user_number") String userNumber, @Field("user_password") String userPassword, @Field("comment_content") String commentContent, @Field("comment_author_name") String commentAuthoName);

    @GET(Const.APT_SEARCH_STUDENT)
    Observable<Student.StudentWrapper> getStudent(@Query("stu") String stu);

    @FormUrlEncoded
    @POST(Const.API_EMPTYROOM)
    Observable<Empty> getEmptyRoomList(@Field("buildNum") String buildNum, @Field("week") String week, @Field("weekdayNum") String weekdayNum, @Field("sectionNum") String sectionNum);


}
