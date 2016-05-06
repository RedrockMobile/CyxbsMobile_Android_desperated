package com.mredrock.cyxbsmobile.network.service;

import com.mredrock.cyxbsmobile.model.RestaurantComment;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.RestaurantDetail;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.model.EatWhat;
import com.mredrock.cyxbsmobile.model.MovieResult;
import com.mredrock.cyxbsmobile.model.Restaurant;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.model.Empty;
import com.mredrock.cyxbsmobile.model.Exam;
import com.mredrock.cyxbsmobile.model.Grade;
import com.mredrock.cyxbsmobile.model.AboutMe;
import com.mredrock.cyxbsmobile.model.Student;
import com.mredrock.cyxbsmobile.model.Subject;

import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.community.BBDDDetail;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
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
    Observable<RedrockApiWrapper<Object>> sendRestaurantComment(@Field("shop_id") String id, @Field("user_number") String userNumber, @Field("user_password") String userPassword, @Field("comment_content") String commentContent, @Field("comment_author_name") String commentAuthoName);

    @GET(Const.APT_SEARCH_STUDENT)
    Observable<Student.StudentWrapper> getStudent(@Query("stu") String stu);

    @FormUrlEncoded
    @POST(Const.API_EMPTYROOM)
    Observable<Empty> getEmptyRoomList(@Field("buildNum") String buildNum, @Field("week") String week, @Field("weekdayNum") String weekdayNum, @Field("sectionNum") String sectionNum);

    @FormUrlEncoded
    @POST(Const.API_SCORE)
    Observable<Grade.GradeWrapper> getGrade(@Field("stuNum") String stuNum, @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_EXAM_SCHEDULE)
    Observable<Exam.ExamWapper> getExam(@Field("stuNum") String stu);

    @FormUrlEncoded
    @POST(Const.API_REEXAM_SCHEDULE)
    Observable<Exam.ExamWapper> getReExam(@Field("stu") String stu);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<OkResponse> setPersonInfo(@Field("stuNum") String stuNum,
                                         @Field("idNum") String idNum,
                                         @Field("photo_thumbnail_src") String photo_thumbnail_src,
                                         @Field("photo_src") String photo_src);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<OkResponse> setPersonNickName(@Field("stuNum") String stuNum,
                                                 @Field("idNum") String idNum,
                                                 @Field("nickname") String nickname);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<OkResponse> setPersonQQ(@Field("stuNum") String stuNum,
                                       @Field("idNum") String idNum,
                                       @Field("qq") String qq);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<OkResponse> setPersonPhone(@Field("stuNum") String stuNum,
                                          @Field("idNum") String idNum,
                                          @Field("phone") String phone);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<OkResponse> setPersonIntroduction(@Field("stuNum") String stuNum,
                                             @Field("idNum") String idNum,
                                             @Field("introduction") String introduction);

    @FormUrlEncoded
    @POST(Const.API_GET_INFO)
    Observable<User.UserWrapper> getPersonInfo(@Field("stuNum") String stuNum,
                                               @Field("idNum") String idNum );

    @FormUrlEncoded
    @POST(Const.API_ABOUT_ME)
    Observable<AboutMe.AboutMeWapper> getAboutMe(@Field("stuNum") String
                                                            stuNum,
                                                   @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_TREND_DETAIL)
    Observable<BBDDDetail.BBDDDetailWrapper> getTrendDetail(@Field("stuNum") String stuNum,
                                          @Field("idNum") String idNum,
                                          @Field("type_id") int type_id,
                                          @Field("article_id") String article_id);

    @FormUrlEncoded
    @POST(Const.API_SEARCH_ARTICLE)
    Observable<BBDDDetail.BBDDDetailWrapper> searchTrends(@Field("stuNum") String stuNum,
                                                @Field("idNum") String idNum);

    Observable<BBDDDetail.BBDDDetailWrapper> searchOtherTrends(@Field("stuNum") String stuNum,
                                                     @Field("idNum") String idNum,
                                                     @Field("stunum_other") String stunum_other);
}
