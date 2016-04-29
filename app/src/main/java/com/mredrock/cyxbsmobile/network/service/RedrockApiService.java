package com.mredrock.cyxbsmobile.network.service;

import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.model.Empty;
import com.mredrock.cyxbsmobile.model.Exam;
import com.mredrock.cyxbsmobile.model.Grade;
import com.mredrock.cyxbsmobile.model.MovieResult;
import com.mredrock.cyxbsmobile.model.RelateMe;
import com.mredrock.cyxbsmobile.model.Student;
import com.mredrock.cyxbsmobile.model.Subject;

import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.model.community.Trend;
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
    Observable<OkResponse> setPersonIntroduction(@Field("stuNum") String stuNum,
                                             @Field("idNum") String idNum,
                                             @Field("introduction") String introduction);

    @FormUrlEncoded
    @POST(Const.API_GET_INFO)
    Observable<User.UserWrapper> getPersonInfo(@Field("stuNum") String stuNum,
                                               @Field("idNum") String idNum );

    @FormUrlEncoded
    @POST(Const.API_ABOUT_ME)
    Observable<RelateMe.RelateMeWapper> getAboutMe(@Field("stuNum") String stuNum,
                                                   @Field("idNum") String idNum,
                                                   @Field("page") int page,
                                                   @Field("size") int size);

    @FormUrlEncoded
    @POST(Const.API_SEARCH_ARTICLE)
    Observable<Trend.TrendWrapper> searchTrends(@Field("stuNum") String stuNum,
                                                @Field("idNum") String idNum,
                                                @Field("page") int page,
                                                @Field("size") int size);

    Observable<Trend.TrendWrapper> searchOtherTrends(@Field("stuNum") String stuNum,
                                                     @Field("idNum") String idNum,
                                                     @Field("page") int page,
                                                     @Field("size") int size,
                                                     @Field("stunum_other") String stunum_other);
}
