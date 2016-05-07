package com.mredrock.cyxbsmobile.network;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.BuildConfig;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.AboutMe;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.model.Exam;
import com.mredrock.cyxbsmobile.model.Food;
import com.mredrock.cyxbsmobile.model.FoodComment;
import com.mredrock.cyxbsmobile.model.FoodDetail;
import com.mredrock.cyxbsmobile.model.Grade;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.model.Shake;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.social.BBDDDetail;
import com.mredrock.cyxbsmobile.model.social.BBDDNews;
import com.mredrock.cyxbsmobile.model.social.BBDDNewsContent;
import com.mredrock.cyxbsmobile.model.social.CommentContent;
import com.mredrock.cyxbsmobile.model.social.HotNews;
import com.mredrock.cyxbsmobile.model.social.OfficeNewsContent;
import com.mredrock.cyxbsmobile.model.social.PersonInfo;
import com.mredrock.cyxbsmobile.model.social.PersonLatest;
import com.mredrock.cyxbsmobile.model.social.Stu;
import com.mredrock.cyxbsmobile.model.social.UploadImgResponse;
import com.mredrock.cyxbsmobile.network.exception.RedrockApiException;
import com.mredrock.cyxbsmobile.network.func.RedrockApiWrapperFunc;
import com.mredrock.cyxbsmobile.network.func.UserInfoVerifyFunc;
import com.mredrock.cyxbsmobile.network.service.RedrockApiService;
import com.mredrock.cyxbsmobile.network.service.UpDownloadService;
import com.mredrock.cyxbsmobile.util.BitmapUtil;
import com.mredrock.cyxbsmobile.util.OkHttpUtils;
import com.mredrock.cyxbsmobile.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * RequestManager
 * 请求服务在 {@link RedrockApiService} 与 {@link UpDownloadService} 中定义
 * Created by cc on 16/1/20.
 */
public enum RequestManager {

    INSTANCE;

    private static final int DEFAULT_TIMEOUT = 30;
    private UpDownloadService upDownloadService;
    private RedrockApiService redrockApiService;
    private CacheProviders cacheProviders;

    RequestManager() {
        OkHttpClient client = configureOkHttp(new OkHttpClient.Builder());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.END_POINT_REDROCK)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        cacheProviders = new RxCache.Builder()
                .persistence(APP.getContext().getFilesDir())
                .using(CacheProviders.class);

        upDownloadService = retrofit.create(UpDownloadService.class);
        redrockApiService = retrofit.create(RedrockApiService.class);
    }

    public static RequestManager getInstance() {
        return INSTANCE;
    }

    public OkHttpClient configureOkHttp(OkHttpClient.Builder builder) {
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }

    public void download(String url, Subscriber<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = upDownloadService.download(url);

        emitObservable(observable, subscriber);
    }

    /* 测试上传文件 */
    @Deprecated
    public void uploadTest(Subscriber<String> subscriber, Uri fileUri) {
        Observable<String> observable =
                upDownloadService.uploadTest(
                    /* 请求地址 */UpDownloadService.TEST_UPLOAD_URL,
                    /* 除了文件，其他POST参数 *///OkHttpUtils.createStringRequestBody("values"),
                    /* 文件，"file"是参数名 */OkHttpUtils.createFileRequestBody("file", fileUri))
                        .map(wrapper -> wrapper.info);

        emitObservable(observable, subscriber);
    }

    public void verify(Subscriber<User> subscriber, String stuNum, String idNum) {
        Observable<User> observable = redrockApiService.verify(stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>());

        emitObservable(observable, subscriber);
    }

    public void getNowWeek(Subscriber<Integer> subscriber, String stuNum, String idNum) {
        Observable<Integer> observable = redrockApiService.getCourse(stuNum, idNum, "0")
                .map(courseWrapper -> {
                    if (courseWrapper.status != Const.REDROCK_API_STATUS_SUCCESS) {
                        throw new RedrockApiException();
                    }
                    //Toast.makeText(APP.getContext(), courseWrapper.nowWeek, Toast.LENGTH_SHORT).show();
                    return Integer.parseInt(courseWrapper.nowWeek);
                });
        emitObservable(observable, subscriber);
    }

    public void getAllCourseJson(Subscriber<String> subscriber, String stuNum, String idNum) {
        getCourseJson(subscriber, stuNum, idNum, "0");
    }

    public void getCourseJson(Subscriber<String> subscriber,
                              String stuNum,
                              String idNum,
                              String week) {
        Observable<String> observable = redrockApiService.getCourse(stuNum, idNum, week)
                .map(courseWrapper -> {
                    return new Gson().toJson(courseWrapper);

                });
        emitObservable(observable, subscriber);
    }

    public Subscription getShake(Subscriber<Shake> subscriber) {
        Observable<Shake> observable = redrockApiService.getShake()
                .map(new RedrockApiWrapperFunc<>());

        return emitObservable(observable, subscriber);
    }

    public Subscription getFoodList(Subscriber<List<Food>> subscriber, String page) {
        Observable<List<Food>> observable = redrockApiService.getFoodList(page)
                .map(new RedrockApiWrapperFunc<>())
                .flatMap(foodList -> {
                    for (Food food : foodList) {
                        redrockApiService.getFoodDetail(food.id)
                                .map(new RedrockApiWrapperFunc<>())
                                .filter(foodDetail -> foodDetail != null)
                                .subscribe(foodDetail -> {
                                    food.recommend = foodDetail.shop_sale_content;
                                });
                    }

                    return Observable.just(foodList);
                });

        return emitObservable(observable, subscriber);
    }

    public Subscription getFoodAndCommentList(Subscriber<FoodDetail> subscriber
            , String shopId, String page) {
        Observable<FoodDetail> foodObservable =
                redrockApiService.getFoodDetail(shopId)
                        .map(new RedrockApiWrapperFunc<>())
                        .filter(foodDetail -> foodDetail != null);

        Observable<List<FoodComment>> foodCommentObservable =
                redrockApiService.getFoodComments(shopId, page)
                        .map(new RedrockApiWrapperFunc<>())
                        .filter(foodCommentList -> Utils.checkNotNullAndNotEmpty(foodCommentList))
                        .flatMap(Observable::from)
                        .toSortedList();


        Observable<FoodDetail> observable =
                Observable.zip(foodObservable, foodCommentObservable, (foodDetail, foodCommentList) -> {
                    foodDetail.foodComments = foodCommentList;
                    return foodDetail;
                });

        return emitObservable(observable, subscriber);
    }

    public Subscription getFood(Subscriber<FoodDetail> subscriber, String restaurantKey) {
        Observable<FoodDetail> observable =
                redrockApiService.getFoodDetail(restaurantKey)
                .map(new RedrockApiWrapperFunc<>());

        return emitObservable(observable, subscriber);
    }

    public Subscription sendCommentAndRefresh(Subscriber<List<FoodComment>> subscriber
            , String shopId, String userId, String userPassword, String content, String authorName) {
        Observable<RedrockApiWrapper<Object>> sendObservable = redrockApiService
                .sendFoodComment(shopId, userId, userPassword, content, authorName);

        Observable<List<FoodComment>> foodCommentsObservable =
                redrockApiService.getFoodComments(shopId, "1")
                        .map(new RedrockApiWrapperFunc<>())
                        .filter(foodCommentList -> Utils.checkNotNullAndNotEmpty(foodCommentList))
                        .flatMap(Observable::from)
                        .toSortedList();

        Observable<List<FoodComment>> observable =
                Observable.zip(sendObservable, foodCommentsObservable, (objectRedrockApiWrapper, restaurantComments) -> {
                    if (objectRedrockApiWrapper.status == 200) {
                        return restaurantComments;
                    } else {
                        return null;
                    }
                });
        return emitObservable(observable, subscriber);
    }

    public Subscription getFoodCommentList(Subscriber<List<FoodComment>> subscriber
            , String shopId, String page) {
        Observable<List<FoodComment>> observable =
                redrockApiService.getFoodComments(shopId, page)
                        .map(new RedrockApiWrapperFunc<>())
                        .filter(foodCommentList -> Utils.checkNotNullAndNotEmpty(foodCommentList))
                        .flatMap(Observable::from)
                        .toSortedList();

        return emitObservable(observable, subscriber);
    }

    public void getCourse(Subscriber<List<Course>> subscriber, String stuNum, String
            idNum, String week){
            Observable<List<Course>> observable = redrockApiService.getCourse(stuNum, idNum, week).map(new RedrockApiWrapperFunc<>());
        }
    public void getCourse(Subscriber<List<Course>> subscriber,
                          List<String> stuNumList, String week) {
        Observable<List<Course>> observable = Observable.from(stuNumList)
                .flatMap(s -> redrockApiService.getCourse(s, "", week))
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }


    public void getStudent(Subscriber<List<com.mredrock.cyxbsmobile.model.Student>> subscriber,
                           String stu) {
        Observable<List<com.mredrock.cyxbsmobile.model.Student>> observable = redrockApiService.getStudent(stu)
                .map(studentWrapper -> studentWrapper.data);
        emitObservable(observable, subscriber);
    }

    public void getEmptyRoomList(Subscriber<List<String>> subscriber, String
            buildNum, String week, String weekdayNum, String sectionNum) {
        Observable<List<String>> observable = redrockApiService
                .getEmptyRoomList(buildNum, week, weekdayNum, sectionNum)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }

    public void getGradeList(Subscriber<List<Grade>> subscriber, String
            stuNum, String stuId, boolean update) {
        Observable<List<Grade>> observable = redrockApiService.getGrade(stuNum, stuId)
                .map(new RedrockApiWrapperFunc<>());
        cacheProviders.getCacheGradeList(observable, new DynamicKey
                (stuNum), new EvictDynamicKey(update))
                .map(Reply::getData);
        emitObservable(observable, subscriber);
    }

    public void getExamList(Subscriber<List<Exam>> subscriber, String
            stu, boolean update) {
        Observable<List<Exam>> observable = redrockApiService.getExam(stu).map(
                examWapper -> examWapper.data);
        cacheProviders.getCacheExamList(observable, new DynamicKey(stu), new
                EvictDynamicKey(update))
                .map(Reply::getData);
        emitObservable(observable, subscriber);
    }

    public void getReExamList(Subscriber<List<Exam>> subscriber, String
            stu, boolean update) {
        Observable<List<Exam>> observable = redrockApiService.getReExam(stu).map(
                examWapper -> examWapper.data);
        cacheProviders.getCacheExamList(observable, new DynamicKey(stu), new
                EvictDynamicKey(update))
                .map(Reply::getData);
        emitObservable(observable, subscriber);
    }


    public Observable<List<AboutMe>> getAboutMeList(String stuNum, String idNum, boolean update) {

        return getAboutMeList(stuNum, idNum).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
//        return cacheProviders.getCacheRelateMes(getAboutMeList(stuNum, idNum), new DynamicKey(stuNum), new EvictDynamicKey(update))
//                             .map(Reply::getData)
//                             .subscribeOn(Schedulers.io())
//                             .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<AboutMe>> getAboutMeList(String stuNum, String idNum) {
        return redrockApiService.getAboutMe(stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<HotNews>> getTrendDetail(String stuNum, String idNum, int type_id, String article_id) {

        List<HotNews> newsList = new ArrayList<>();

        return redrockApiService.getTrendDetail(stuNum, idNum, type_id, article_id)
                .flatMap(bbddDetailWrapper -> Observable.from(bbddDetailWrapper.data))
                .map(bbddDetail -> {
                    HotNews news = new HotNews(bbddDetail);
                    newsList.add(news);
                    return newsList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<HotNews>> getMyTrend(String stuNum, String idNum) {
        List<HotNews> newsList = new ArrayList<>();
        return redrockApiService.searchTrends(stuNum, idNum)
                .flatMap(bbddDetailWrapper -> Observable.from(bbddDetailWrapper.data))
                .map(bbddDetail -> {
                    HotNews news = new HotNews(bbddDetail);
                    newsList.add(news);
                    return newsList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 社区api
     */
    public Observable<UploadImgResponse.Response> uploadNewsImg(String filePath) {
        return uploadNewsImg(Stu.STU_NUM, filePath);
    }

    public Observable<UploadImgResponse.Response> uploadNewsImg(String stuNum, String filePath) {

        File file = new File(filePath);
        try {
            file = BitmapUtil.decodeBitmapFromRes(APP.getContext(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part file_body = MultipartBody.Part.createFormData("fold", file.getName(), requestFile);
        RequestBody stuNum_body = RequestBody.create(MediaType.parse("multipart/form-data"), stuNum);
        return redrockApiService.uploadSocialImg(stuNum_body, file_body)
                .map(new RedrockApiWrapperFunc<>());

    }

    public Observable<List<HotNews>> getHotArticle(int size, int page, boolean update) {
        return cacheProviders.getCacheNews(getHotArticle(size, page), new DynamicKeyGroup(size, page), new EvictDynamicKey(update))
                .map(Reply::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<HotNews>> getHotArticle(int size, int page) {
        return getHotArticle(size, page, Stu.STU_NUM, Stu.ID_NUM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<HotNews>> getHotArticle(int size,
                                                   int page,
                                                   String stuNum,
                                                   String idNum) {
        return redrockApiService.getSocialHotList(size, page, stuNum, idNum);
    }


    public Observable<List<OfficeNewsContent>> getListNews(int size,
                                                           int page,
                                                           String stuNum,
                                                           String idNum,
                                                           String type_id) {
        return redrockApiService.getSocialOfficialNewsList(size, page, stuNum, idNum, type_id)
                .map(new RedrockApiWrapperFunc<>());
    }

    public Observable<List<HotNews>> getListNews(int size, int page) {
        return getListNews(size, page, Stu.STU_NUM, Stu.ID_NUM, BBDDNews.LISTNEWS)
                .map(content -> {
                    List<HotNews> aNews = new ArrayList<>();
                    for (OfficeNewsContent bean : content) aNews.add(new HotNews(bean));
                    return aNews;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<HotNews>> getListNews(int size, int page, boolean update) {
        return cacheProviders.getCacheContentBean(getListNews(size, page), new DynamicKeyGroup(size, page), new EvictDynamicKey(update))
                .map(listReply -> listReply.getData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<HotNews>> getListArticle(int type_id,
                                                    int size,
                                                    int page,
                                                    boolean update) {
        return cacheProviders.getCacheNews(getListArticle(type_id, size, page), new DynamicKeyGroup(type_id, size), new EvictDynamicKey(update))
                .map(listReply -> listReply.getData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<HotNews>> getListArticle(int type_id, int size, int page) {
        return getListArticle(type_id, size, page, Stu.STU_NUM, Stu.ID_NUM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<HotNews>> getListArticle(int type_id,
                                                    int size,
                                                    int page,
                                                    String stuNum,
                                                    String idNum) {
        return redrockApiService.getSocialBBDDList(type_id, size, page, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .flatMap(bbdd -> Observable.just(bbdd)
                        .map(mBBDD -> {
                            List<HotNews> aNews = new ArrayList<>();
                            for (BBDDNewsContent bbddNewsContent : mBBDD)
                                aNews.add(new HotNews(bbddNewsContent));
                            return aNews;
                        }));
    }

    public Observable<String> sendDynamic(int type_id,
                                          String title,
                                          String content,
                                          String thumbnail_src,
                                          String photo_src) {
        return sendDynamic(type_id, title, Stu.UER_ID, content, thumbnail_src, photo_src, Stu.STU_NUM, Stu.ID_NUM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> sendDynamic(int type_id,
                                          String title,
                                          String user_id,
                                          String content,
                                          String thumbnail_src,
                                          String photo_src,
                                          String stuNum,
                                          String idNum) {
        return redrockApiService.sendDynamic(type_id, title, user_id, content, thumbnail_src, photo_src, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>());
    }

    public Observable<List<CommentContent>> getRemarks(String article_id, int type_id) {
        return getRemarks(article_id, type_id, Stu.UER_ID, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<List<CommentContent>> getRemarks(String article_id,
                                                       int type_id,
                                                       String user_id,
                                                       String stuNum,
                                                       String idNum) {
        Log.e("===>>article_id->", article_id);
        Log.e("===>>type_id->", type_id + "");
        return redrockApiService.getSocialCommentList(article_id, type_id, user_id, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> postReMarks(String article_id, int type_id, String content) {
        return postReMarks(article_id, type_id, content, Stu.UER_ID, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<String> postReMarks(String article_id,
                                          int type_id,
                                          String content,
                                          String user_id,
                                          String stuNum,
                                          String idNum) {
        return redrockApiService.addSocialComment(article_id, type_id, content, user_id, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> addThumbsUp(String article_id, int type_id) {
        return addThumbsUp(article_id, type_id, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<String> addThumbsUp(String article_id,
                                          int type_id,
                                          String stuNum,
                                          String idNum) {
        return redrockApiService.socialLike(article_id, type_id, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> cancelThumbsUp(String article_id, int type_id) {
        return cancelThumbsUp(article_id, type_id, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<String> cancelThumbsUp(String article_id,
                                             int type_id,
                                             String stuNum,
                                             String idNum) {
        return redrockApiService.socialUnlike(article_id, type_id, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("unchecked")
    public Observable<RedrockApiWrapper> setPersonInfo(String stuNum, String idNum, String photo_thumbnail_src, String photo_src) {

        return redrockApiService.setPersonInfo(stuNum, idNum, photo_thumbnail_src, photo_src);
    }

    @SuppressWarnings("unchecked")
    public void setPersonNickName(Subscriber<RedrockApiWrapper<Object>> subscriber, String stuNum, String idNum, String nickName) {

        Observable<RedrockApiWrapper<Object>> observable = redrockApiService.setPersonNickName(stuNum, idNum, nickName);

        emitObservable(observable, subscriber);
    }

    public Observable<PersonInfo> getPersonInfo(String otherStuNum, String stuNum, String idNum) {
        return redrockApiService.getPersonInfo(otherStuNum, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<PersonInfo> getPersonInfo(String otherStuNum) {
        return getPersonInfo(otherStuNum, Stu.STU_NUM, Stu.ID_NUM);
    }


    public Observable<List<HotNews>> getPersonLatestList(String otherStuNum, String stuNum, String idNum, String userName, String userHead) {

        return redrockApiService.getPersonLatestList(otherStuNum, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .map(personLatests -> {
                    List<HotNews> aNews = new ArrayList<>();
                    for (PersonLatest personLatest : personLatests)
                        aNews.add(new HotNews(personLatest, otherStuNum, userName, userHead));
                    return aNews;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<HotNews>> getPersonLatestList(String otherStuNum, String userName, String userHead) {
        return getPersonLatestList(otherStuNum, Stu.STU_NUM, Stu.ID_NUM, userName, userHead);
    }

    @SuppressWarnings("unchecked")
    public void setPersonIntroduction(Subscriber<RedrockApiWrapper<Object>> subscriber, String stuNum, String idNum, String introduction) {

        Observable<RedrockApiWrapper<Object>> observable = redrockApiService.setPersonIntroduction(stuNum, idNum, introduction);

        emitObservable(observable, subscriber);
    }

    @SuppressWarnings("unchecked")
    public void setPersonQQ(Subscriber<RedrockApiWrapper<Object>> subscriber, String stuNum, String idNum, String qq) {

        Observable<RedrockApiWrapper<Object>> observable = redrockApiService.setPersonQQ(stuNum, idNum, qq)
                .map(new RedrockApiWrapperFunc());

        emitObservable(observable, subscriber);
    }

    @SuppressWarnings("unchecked")
    public void setPersonPhone(Subscriber<RedrockApiWrapper<Object>> subscriber, String stuNum, String idNum, String phone) {

        Observable<RedrockApiWrapper<Object>> observable = redrockApiService.setPersonPhone(stuNum, idNum, phone)
                .map(new RedrockApiWrapperFunc());

        emitObservable(observable, subscriber);
    }

    public void getPersonInfo(Subscriber<User> subscriber, String stuNum, String idNum) {
        Observable<User> observable = redrockApiService.getPersonInfo(stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .map(new UserInfoVerifyFunc());
        emitObservable(observable, subscriber);
    }

    private <T> Subscription emitObservable(Observable<T> o, Subscriber<T> s) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}

