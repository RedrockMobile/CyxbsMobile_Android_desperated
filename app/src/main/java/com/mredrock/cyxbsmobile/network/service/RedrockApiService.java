package com.mredrock.cyxbsmobile.network.service;

import com.mredrock.cyxbsmobile.model.FoodComment;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.FoodDetail;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.model.Shake;
import com.mredrock.cyxbsmobile.model.MovieResult;
import com.mredrock.cyxbsmobile.model.Food;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.model.Empty;
import com.mredrock.cyxbsmobile.model.Student;
import com.mredrock.cyxbsmobile.model.Subject;

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

    //Explore start
    @GET(Const.API_SHAKE)
    Observable<RedrockApiWrapper<Shake>> getShake();

    @FormUrlEncoded
    @POST(Const.API_FOOD_LIST)
    Observable<RedrockApiWrapper<List<Food>>> getFoodList(@Field("pid") String page);

    @FormUrlEncoded
    @POST(Const.API_FOOD_DETAIL)
    Observable<RedrockApiWrapper<FoodDetail>> getFoodDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST(Const.API_FOOD_COMMENT_LIST)
    Observable<RedrockApiWrapper<List<FoodComment>>> getFoodComments(@Field("shop_id") String id, @Field("pid") String page);

    @FormUrlEncoded
    @POST(Const.API_SEND_FOOD_COMMENT)
    Observable<RedrockApiWrapper<Object>> sendFoodComment(@Field("shop_id") String id, @Field("user_number") String userNumber, @Field("user_password") String userPassword,@Field("comment_content") String commentContent,@Field("comment_author_name") String commentAuthoName);
    //Explore end

    @GET(Const.APT_SEARCH_STUDENT)
    Observable<Student.StudentWrapper> getStudent(@Query("stu") String stu);

    @FormUrlEncoded
    @POST(Const.API_EMPTYROOM)
    Observable<Empty> getEmptyRoomList(@Field("buildNum") String buildNum, @Field("week") String week, @Field("weekdayNum") String weekdayNum, @Field("sectionNum") String sectionNum);


}
