package com.mredrock.cyxbs.network.service;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.AboutMe;
import com.mredrock.cyxbs.model.AffairApi;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.Empty;
import com.mredrock.cyxbs.model.Exam;
import com.mredrock.cyxbs.model.Food;
import com.mredrock.cyxbs.model.FoodComment;
import com.mredrock.cyxbs.model.FoodDetail;
import com.mredrock.cyxbs.model.Grade;
import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.model.Shake;
import com.mredrock.cyxbs.model.Student;
import com.mredrock.cyxbs.model.UpdateInfo;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.BBDDDetail;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.Comment;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.OfficeNews;
import com.mredrock.cyxbs.model.social.PersonInfo;
import com.mredrock.cyxbs.model.social.PersonLatest;
import com.mredrock.cyxbs.model.social.RequestResponse;
import com.mredrock.cyxbs.model.social.UploadImgResponse;
import com.mredrock.cyxbs.network.setting.annotation.XmlApi;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cc on 16/1/20.
 */
public interface RedrockApiService {

    @GET(Const.API_UPDATE_OLD)
    @XmlApi
    Observable<UpdateInfo> update();

    @FormUrlEncoded
    @POST(Const.API_VERIFY)
    Observable<User.UserWrapper> verify(@Field("stuNum") String stuNum, @Field("idNum") String idNum);

    @FormUrlEncoded
    @Headers("API_APP: android")
    @POST(Const.API_PERSON_SCHEDULE)
    Observable<Course.CourseWrapper> getCourse(@Field("stuNum") String stuNum,
                                               @Field("idNum") String idNum,
                                               @Field("week") String week);

    @FormUrlEncoded
    @Headers("API_APP: android")
    @POST(Const.API_PERSON_SCHEDULE)
    retrofit2.Call<Course.CourseWrapper> getCourseCall(@Field("stuNum") String stuNum,
                                                   @Field("idNum") String idNum,
                                                   @Field("week") String week);

    //Explore start
    @FormUrlEncoded
    @POST(Const.API_MAP_PICTURE)
    Observable<RedrockApiWrapper<List<String>>> getMapOverlayImageUrl(@Field("name") String name, @Field("path") String path);

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
    Observable<RedrockApiWrapper<List<FoodComment>>> getFoodComments(@Field("shop_id") String id,
                                                                     @Field("pid") String page);

    @FormUrlEncoded
    @POST(Const.API_SEND_FOOD_COMMENT)
    Observable<RedrockApiWrapper<Object>> sendFoodComment(@Field("shop_id") String id,
                                                          @Field("user_number") String userNumber,
                                                          @Field("user_password") String userPassword,
                                                          @Field("comment_content") String commentContent,
                                                          @Field("comment_author_name") String commentAuthoName);
    //Explore end

    @GET(Const.APT_SEARCH_STUDENT)
    Observable<Student.StudentWrapper> getStudent(@Query("stu") String stu);

    @FormUrlEncoded
    @POST(Const.API_EMPTYROOM)
    Observable<Empty> getEmptyRoomList(@Field("buildNum") String buildNum,
                                       @Field("week") String week,
                                       @Field("weekdayNum") String weekdayNum,
                                       @Field("sectionNum") String sectionNum);

    @FormUrlEncoded
    @POST(Const.API_SCORE)
    Observable<Grade.GradeWrapper> getGrade(@Field("stuNum") String stuNum,
                                            @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_EXAM_SCHEDULE)
    Observable<Exam.ExamWapper> getExam(@Field("stuNum") String stu);

    @FormUrlEncoded
    @POST(Const.API_REEXAM_SCHEDULE)
    Observable<Exam.ExamWapper> getReExam(@Field("stu") String stu);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<RedrockApiWrapper> setPersonInfo(@Field("stuNum") String stuNum,
                                                @Field("idNum") String idNum,
                                                @Field("photo_thumbnail_src") String photo_thumbnail_src,
                                                @Field("photo_src") String photo_src);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<RedrockApiWrapper<Object>> setPersonNickName(@Field("stuNum") String stuNum,
                                                            @Field("idNum") String idNum,
                                                            @Field("nickname") String nickname);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<RedrockApiWrapper<Object>> setPersonQQ(@Field("stuNum") String stuNum,
                                                      @Field("idNum") String idNum,
                                                      @Field("qq") String qq);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<RedrockApiWrapper<Object>> setPersonPhone(@Field("stuNum") String stuNum,
                                                         @Field("idNum") String idNum,
                                                         @Field("phone") String phone);

    @FormUrlEncoded
    @POST(Const.API_EDIT_INFO)
    Observable<RedrockApiWrapper<Object>> setPersonIntroduction(@Field("stuNum") String stuNum,
                                                                @Field("idNum") String idNum,
                                                                @Field("introduction") String introduction);

    @FormUrlEncoded
    @POST(Const.API_GET_INFO)
    Observable<User.UserWrapper> getPersonInfo(@Field("stuNum") String stuNum,
                                               @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_ABOUT_ME)
    Observable<AboutMe.AboutMeWapper> getAboutMe(@Field("stuNum") String
                                                         stuNum,
                                                 @Field("idNum") String idNum);

    // TODO: Modified
    @FormUrlEncoded
    @POST(Const.API_TREND_DETAIL)
    Observable<BBDDDetail.BBDDDetailWrapper> getTrendDetail(@Field("type_id") int type_id,
                                                            @Field("article_id") String article_id);

    @FormUrlEncoded
    @POST(Const.API_SEARCH_ARTICLE)
    Observable<BBDDDetail.BBDDDetailWrapper> searchTrends(@Field("stuNum") String stuNum,
                                                          @Field("idNum") String idNum);

    Observable<BBDDDetail.BBDDDetailWrapper> searchOtherTrends(@Field("stuNum") String stuNum,
                                                               @Field("idNum") String idNum,
                                                               @Field("stunum_other") String stunum_other);

    // TODO: API Modified
    @FormUrlEncoded
    @POST(Const.API_SOCIAL_OFFICIAL_NEWS_LIST)
    Observable<OfficeNews> getSocialOfficialNewsList(@Field("size") int size,
                                                     @Field("page") int page);

    // TODO: Modified
    @FormUrlEncoded
    @POST(Const.API_SOCIAL_HOT_LIST)
    Observable<List<HotNews>> getSocialHotList(@Field("size") int size,
                                               @Field("page") int page);

    // TODO: Modified
    @FormUrlEncoded
    //哔哔叨叨(或者其他的)接口：POST
    @POST(Const.API_SOCIAL_BBDD_LIST)
    Observable<BBDDNews> getSocialBBDDList(@Field("type_id") int type_id,
                                           @Field("size") int size,
                                           @Field("page") int page);


    @Multipart
    @POST(Const.API_SOCIAL_IMG_UPLOAD)
    Observable<UploadImgResponse> uploadSocialImg(@Part("stunum") RequestBody stunum,
                                                  @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(Const.API_SOCIAL_ARTICLE_ADD)
    Observable<RequestResponse> sendDynamic(@Field("type_id") int type_id,
                                            @Field("title") String title,
                                            @Field("user_id") String user_id,
                                            @Field("content") String content,
                                            @Field("thumbnail_src") String thumbnail_src,
                                            @Field("photo_src") String photo_src,
                                            @Field("stuNum") String stuNum,
                                            @Field("idNum") String idNum);

    // TODO: Modified
    @FormUrlEncoded
    @POST(Const.API_SOCIAL_COMMENT_LIST)
    Observable<Comment> getSocialCommentList(@Field("article_id") String article_id,
                                             @Field("type_id") int type_id);


    @FormUrlEncoded
    @POST(Const.API_SOCIAL_COMMENT_ADD)
    Observable<RequestResponse> addSocialComment(@Field("article_id") String article_id,
                                                 @Field("type_id") int type_id,
                                                 @Field("content") String content,
                                                 @Field("user_id") String user_id,
                                                 @Field("stuNum") String stuNum,
                                                 @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_SOCIAL_LIKE)
    Observable<RequestResponse> socialLike(@Field("article_id") String article_id,
                                           @Field("type_id") int type_id,
                                           @Field("stuNum") String stuNum,
                                           @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_SOCIAL_UNLIKE)
    Observable<RequestResponse> socialUnlike(@Field("article_id") String article_id,
                                             @Field("type_id") int type_id,
                                             @Field("stuNum") String stuNum,
                                             @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_GET_PERSON_INFO)
    Observable<RedrockApiWrapper<PersonInfo>> getPersonInfo(@Field("stunum_other") String otherStuNum,
                                                            @Field("stuNum") String stuNum,
                                                            @Field("idNum") String idNum);

    // TODO: Modified
    @FormUrlEncoded
    @POST(Const.API_GET_PERSON_LATEST)
    Observable<RedrockApiWrapper<List<PersonLatest>>> getPersonLatestList(@Field("stunum_other") String otherStuNum);

    @FormUrlEncoded
    @POST(Const.API_GET_AFFAIR)
    Observable<AffairApi<List<AffairApi.AffairItem>>> getAffair(@Field("stuNum") String stuNum, @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST(Const.API_ADD_AFFAIR)
    Observable<RedrockApiWrapper<Object>> addAffair(@Field("id")String id, @Field("stuNum") String stuNum, @Field("idNum") String idNum,
                                            @Field("date") String date, @Field("time")int time,@Field("title")String title,
                                            @Field("content")String content);

    @FormUrlEncoded
    @POST(Const.API_EDIT_AFFAIR)
    Observable<RedrockApiWrapper<Object>> editAffair(@Field("id")String id, @Field("stuNum") String stuNum, @Field("idNum") String idNum,
                                            @Field("date") String date, @Field("time")int time,@Field("title")String title,
                                            @Field("content")String content);

    @FormUrlEncoded
    @POST(Const.API_DELETE_AFFAIR)
    Observable<RedrockApiWrapper<Object>> deleteAffair(@Field("stuNum") String stuNum, @Field("idNum") String idNum, @Field("id") String id);

}