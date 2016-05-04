package com.mredrock.cyxbsmobile.network;

import android.net.Uri;

import com.google.gson.Gson;
import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.BuildConfig;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.model.EatWhat;
import com.mredrock.cyxbsmobile.model.MovieResult;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.model.Restaurant;
import com.mredrock.cyxbsmobile.model.RestaurantComment;
import com.mredrock.cyxbsmobile.model.RestaurantDetail;
import com.mredrock.cyxbsmobile.model.Subject;
import com.mredrock.cyxbsmobile.model.community.BBDDNews;
import com.mredrock.cyxbsmobile.model.community.Comment;

import com.mredrock.cyxbsmobile.model.community.ContentBean;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.model.community.OfficeNews;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.model.community.Stu;
import com.mredrock.cyxbsmobile.model.community.UploadImgResponse;
import com.mredrock.cyxbsmobile.network.exception.ApiException;
import com.mredrock.cyxbsmobile.network.exception.RedrockApiException;
import com.mredrock.cyxbsmobile.network.service.NewsApiService;
import com.mredrock.cyxbsmobile.network.service.RedrockApiService;
import com.mredrock.cyxbsmobile.network.service.UpDownloadService;
import com.mredrock.cyxbsmobile.util.BitmapUtil;
import com.mredrock.cyxbsmobile.util.OkHttpUtils;
import com.mredrock.cyxbsmobile.util.Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
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
    private NewsApiService newsApiService;

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
        newsApiService = retrofit.create(NewsApiService.class);
    }

    public static RequestManager getInstance() {
        return INSTANCE;
    }

    public OkHttpClient configureOkHttp(OkHttpClient.Builder builder) {
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
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

    public void getTopMovie(Subscriber<List<Subject>> subscriber, int start, int count) {
        Observable<List<Subject>> observable = redrockApiService.getTopMovie(RedrockApiService.MOVIE_URL, start, count)
                .map(new MovieResultFunc<>());

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

    public void getCourseJson(Subscriber<String> subscriber, String stuNum, String idNum, String week) {
        Observable<String> observable = redrockApiService.getCourse(stuNum, idNum, week)
                .map(courseWrapper -> {
                    Logger.d(courseWrapper.nowWeek);
                    return new Gson().toJson(courseWrapper);

                });
        emitObservable(observable, subscriber);
    }

    public Subscription getEatWhat(Subscriber<EatWhat> subscriber) {
        Observable<EatWhat> observable = redrockApiService.getEatWhat()
                .map(new RedrockApiWrapperFunc<>())
                .filter(Utils::checkNotNull);
        return emitObservable(observable, subscriber);
    }

    public Subscription getRestaurantList(Subscriber<List<Restaurant>> subscriber, String page) {
        Observable<List<Restaurant>> observable = redrockApiService.getRestaurantList(page)
                .map(new RedrockApiWrapperFunc<>())
                .filter(Utils::checkNotNullAndNotEmpty)
                .flatMap(restaurants -> {
                    for (Restaurant restaurant : restaurants) {
                        redrockApiService.getRestaurantDetail(restaurant.id)
                                .map(new RedrockApiWrapperFunc<>())
                                .filter(restaurantDetail -> restaurantDetail != null)

                                .subscribe(restaurantDetail -> {
                                    restaurant.content = restaurantDetail.shop_sale_content;
                                });
                    }
                    return Observable.just(restaurants);
                });

        return emitObservable(observable, subscriber);
    }

    public Subscription getRestaurantAndComments(Subscriber<RestaurantDetail> subscriber
            , String shopId, String page) {
        Observable<RestaurantDetail> restaurantObservable =
                redrockApiService.getRestaurantDetail(shopId)
                        .map(new RedrockApiWrapperFunc<>())
                        .filter(Utils::checkNotNull);
        Observable<List<RestaurantComment>> restaurantCommentObservable =
                redrockApiService.getRestaurantComments(shopId, page)
                        .map(new RedrockApiWrapperFunc<>())
                        .flatMap(Observable::from)
                        .toSortedList();


        Observable<RestaurantDetail> observable =
                Observable.zip(restaurantObservable, restaurantCommentObservable, (restaurantDetail, restaurantComments) -> {
                    restaurantDetail.restaurantComments = restaurantComments;
                    return restaurantDetail;
                });

        return emitObservable(observable, subscriber);
    }

    public Subscription sendRestaurantCommentAndRefresh(Subscriber<List<RestaurantComment>> subscriber
            , String shopId, String userId, String userPassword, String content, String authorName) {
        Observable<RedrockApiWrapper<Object>> sendObservable = redrockApiService
                .sendRestaurantComment(shopId, userId, userPassword, content, authorName);

        Observable<List<RestaurantComment>> restaurantCommentObservable =
                redrockApiService.getRestaurantComments(shopId, "1")
                        .map(new RedrockApiWrapperFunc<>())
                        .flatMap(Observable::from)
                        .toSortedList();

        Observable<List<RestaurantComment>> observable =
                Observable.zip(sendObservable, restaurantCommentObservable, (objectRedrockApiWrapper, restaurantComments) -> {
                    if (objectRedrockApiWrapper.status == 200) {
                        return restaurantComments;
                    } else {
                        return null;

                    }
                });
        return emitObservable(observable, subscriber);
    }

    public void getCourse(Subscriber<List<Course>> subscriber, String stuNum, String
            idNum, String week) {
        Observable<List<Course>> observable = redrockApiService.getCourse(stuNum, idNum, week).map(new RedrockApiWrapperFunc<>());
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

    private <T> Subscription emitObservable(Observable<T> o, Subscriber<T> s) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
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
        return newsApiService.uploadImg(stuNum_body, file_body).map(new RedrockApiWrapperFunc<>());

    }

    public Observable<List<News>> getHotArticle(int size, int page, boolean update) {
        return cacheProviders.getCacheNews(getHotArticle(size, page), new DynamicKeyGroup(size, page), new EvictDynamicKey(update))
                .map(listReply -> listReply.getData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<News>> getHotArticle(int size, int page) {
        return getHotArticle(size, page, Stu.STU_NUM, Stu.ID_NUM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<News>> getHotArticle(int size, int page, String stuNum, String idNum) {
        return newsApiService.getHotArticle(size, page, stuNum, idNum);
    }


    public Observable<OfficeNews> getListNews(int size, int page, String stuNum, String idNum, String type_id) {
        return newsApiService.getlistNews(size, page, stuNum, idNum, type_id);
    }

    public Observable<List<News>> getListNews(int size, int page) {
        return getListNews(size, page, Stu.STU_NUM, Stu.ID_NUM, BBDDNews.LISTNEWS)
                .flatMap(officeNews -> Observable.just(officeNews.data))

                .map(contentBeen -> {
                    List<News> news = new ArrayList<>();
                    for (ContentBean bean : contentBeen) news.add(new News(bean));
                    return news;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<News>> getListNews(int size, int page, boolean update) {
        return cacheProviders.getCacheContentBean(getListNews(size, page), new DynamicKeyGroup(size, page), new EvictDynamicKey(update))
                .map(listReply -> listReply.getData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<News>> getListArticle(int type_id, int size, int page, boolean update) {
        return cacheProviders.getCacheNews(getListArticle(type_id, size, page), new DynamicKeyGroup(type_id, size), new EvictDynamicKey(update))
                .map(listReply -> listReply.getData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<News>> getListArticle(int type_id, int size, int page) {
        return getListArticle(type_id, size, page, Stu.STU_NUM, Stu.ID_NUM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<News>> getListArticle(int type_id, int size, int page, String stuNum, String idNum) {
        return newsApiService.getListArticle(type_id, size, page, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .flatMap(bbddBeen -> Observable.just(bbddBeen).map(new Func1<List<BBDDNews.BBDDBean>, List<News>>() {
                    @Override
                    public List<News> call(List<BBDDNews.BBDDBean> bbddBeen) {
                        List<News> news = new ArrayList<>();
                        for (BBDDNews.BBDDBean mbbddBean : bbddBeen)
                            news.add(new News(mbbddBean));
                        return news;
                    }
                }));
    }

    public Observable<OkResponse> sendDynamic(int type_id, String title, String content, String thumbnail_src, String photo_src) {
        return sendDynamic(type_id, title, Stu.UER_ID, content, thumbnail_src, photo_src, Stu.STU_NUM, Stu.ID_NUM)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OkResponse> sendDynamic(int type_id, String title, String user_id, String content, String thumbnail_src, String photo_src, String stuNum, String idNum) {
        return newsApiService.sendDynamic(type_id, title, user_id, content, thumbnail_src, photo_src, stuNum, idNum);
    }

    public Observable<Comment> getRemarks(String article_id, int type_id) {
        return getRemarks(article_id, type_id, Stu.UER_ID, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<Comment> getRemarks(String article_id, int type_id, String user_id, String stuNum, String idNum) {

        return newsApiService.getReMark(article_id, type_id, user_id, stuNum, idNum).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OkResponse> postReMarks(String article_id, int type_id, String content) {
        return postReMarks(article_id, type_id, content, Stu.UER_ID, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<OkResponse> postReMarks(String article_id, int type_id, String content, String user_id, String stuNum, String idNum) {
        return newsApiService.postReMarks(article_id, type_id, content, user_id, stuNum, idNum).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OkResponse> addThumbsUp(String article_id, int type_id) {
        return addThumbsUp(article_id, type_id, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<OkResponse> addThumbsUp(String article_id, int type_id, String stuNum, String idNum) {
        return newsApiService.addThumbsUp(article_id, type_id, stuNum, idNum).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OkResponse> cancelThumbsUp(String article_id, int type_id) {
        return cancelThumbsUp(article_id, type_id, Stu.STU_NUM, Stu.ID_NUM);
    }

    public Observable<OkResponse> cancelThumbsUp(String article_id, int type_id, String stuNum, String idNum) {
        return newsApiService.cancelThumbsUp(article_id, type_id, stuNum, idNum).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    private class MovieResultFunc<T> implements Func1<MovieResult<T>, T> {

        @Override
        public T call(MovieResult<T> movieResult) {
            if (movieResult.count == 0) {
                throw new ApiException(100);
            }
            return movieResult.subjects;
        }
    }

    private class RedrockApiWrapperFunc<T> implements Func1<RedrockApiWrapper<T>, T> {

        @Override
        public T call(RedrockApiWrapper<T> wrapper) {
            if (wrapper.status != Const.REDROCK_API_STATUS_SUCCESS) {

                throw new RedrockApiException(wrapper.info);
            }
            return wrapper.data;
        }
    }
}

