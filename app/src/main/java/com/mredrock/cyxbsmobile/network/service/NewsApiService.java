package com.mredrock.cyxbsmobile.network.service;

import com.mredrock.cyxbsmobile.model.community.BBDD;
import com.mredrock.cyxbsmobile.model.community.BBDDNews;
import com.mredrock.cyxbsmobile.model.community.Dynamic;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.model.community.OfficeNews;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.model.community.Remark;
import com.mredrock.cyxbsmobile.model.community.UploadImgResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by mathiasluo on 16-4-6.
 */
public interface NewsApiService {

    //热门动态清单接口
    @POST("cyxbsMobile/index.php/Home/Article/searchHotArticle")
    Observable<News> getNews(@Body BBDD bbdd);


    @POST("cyxbsMobile/index.php/Home/Article/addArticle")
    Observable<OkResponse> sendDynamic(@Body Dynamic dynamic);


    @FormUrlEncoded
    @POST("/cyxbsMobile/index.php/Home/Article/listNews")
    Observable<OfficeNews> getlistNews(@Field("size") int size,
                                       @Field("page") int page,
                                       @Field("stuNum") String stuNum,
                                       @Field("idNum") String idNum,
                                       @Field("type_id") String type_id);

    @FormUrlEncoded
    @POST("cyxbsMobile/index.php/Home/Article/searchHotArticle")
    Observable<List<News>> getHotArticle(@Field("size") int size,
                                         @Field("page") int page,
                                         @Field("stuNum") String stuNum,
                                         @Field("idNum") String idNum);

    @FormUrlEncoded
    //哔哔叨叨(或者其他的)接口：POST
    @POST("cyxbsMobile/index.php/Home/Article/listArticle")
    Observable<BBDDNews> getListArticle(@Field("type_id") int type_id,
                                        @Field("size") int size,
                                        @Field("page") int page,
                                        @Field("stuNum") String stuNum,
                                        @Field("idNum") String idNum);


    @Multipart
    @POST("cyxbsMobile/index.php/Home/Photo/uploadArticle")
    Observable<UploadImgResponse> uploadImg(@Part("stunum") RequestBody stunum,
                                            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("cyxbsMobile/index.php/Home/Article/addArticle")
    Observable<OkResponse> sendDynamic(@Field("type_id") int type_id,
                                       @Field("title") String title,
                                       @Field("user_id") String user_id,
                                       @Field("content") String content,
                                       @Field("thumbnail_src") String thumbnail_src,
                                       @Field("photo_src") String photo_src,
                                       @Field("stuNum") String stuNum,
                                       @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST("cyxbsMobile/index.php/Home/ArticleRemark/getremark")
    Observable<Remark> getReMark(@Field("article_id") String article_id,
                                 @Field("type_id") int type_id,
                                 @Field("user_id") String user_id,
                                 @Field("stuNum") String stuNum,
                                 @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST("cyxbsMobile/index.php/Home/ArticleRemark/postremarks")
    Observable<OkResponse> postReMarks(@Field("article_id") String article_id,
                                       @Field("type_id") int type_id,
                                       @Field("content") String content,
                                       @Field("user_id") String user_id,
                                       @Field("stuNum") String stuNum,
                                       @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST("cyxbsMobile/index.php/Home/Praise/addone")
    Observable<OkResponse> addThumbsUp(@Field("article_id") String article_id,
                                       @Field("type_id") int type_id,
                                       @Field("stuNum") String stuNum,
                                       @Field("idNum") String idNum);

    @FormUrlEncoded
    @POST("cyxbsMobile/index.php/Home/Praise/cancel")
    Observable<OkResponse> cancelThumbsUp(@Field("article_id") String article_id,
                                          @Field("type_id") int type_id,
                                          @Field("stuNum") String stuNum,
                                          @Field("idNum") String idNum);


}
