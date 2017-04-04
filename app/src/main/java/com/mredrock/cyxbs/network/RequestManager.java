package com.mredrock.cyxbs.network;

import android.content.Context;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.BuildConfig;
import com.mredrock.cyxbs.component.remind_service.Reminder;
import com.mredrock.cyxbs.component.remind_service.func.BaseRemindFunc;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.model.AboutMe;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.ElectricCharge;
import com.mredrock.cyxbs.model.Exam;
import com.mredrock.cyxbs.model.Food;
import com.mredrock.cyxbs.model.FoodComment;
import com.mredrock.cyxbs.model.FoodDetail;
import com.mredrock.cyxbs.model.Grade;
import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.model.Shake;
import com.mredrock.cyxbs.model.StartPage;
import com.mredrock.cyxbs.model.UpdateInfo;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.lost.Lost;
import com.mredrock.cyxbs.model.lost.LostDetail;
import com.mredrock.cyxbs.model.lost.LostStatus;
import com.mredrock.cyxbs.model.lost.LostWrapper;
import com.mredrock.cyxbs.model.social.BBDDNewsContent;
import com.mredrock.cyxbs.model.social.CommentContent;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.OfficeNewsContent;
import com.mredrock.cyxbs.model.social.PersonInfo;
import com.mredrock.cyxbs.model.social.PersonLatest;
import com.mredrock.cyxbs.model.social.Topic;
import com.mredrock.cyxbs.model.social.UploadImgResponse;
import com.mredrock.cyxbs.network.exception.RedrockApiException;
import com.mredrock.cyxbs.network.func.AffairTransformFunc;
import com.mredrock.cyxbs.network.func.AffairWeekFilterFunc;
import com.mredrock.cyxbs.network.func.ElectricQueryFunc;
import com.mredrock.cyxbs.network.func.RedrockApiWrapperFunc;
import com.mredrock.cyxbs.network.func.StartPageFunc;
import com.mredrock.cyxbs.network.func.UpdateVerifyFunc;
import com.mredrock.cyxbs.network.func.UserCourseFilterFunc;
import com.mredrock.cyxbs.network.func.UserInfoVerifyFunc;
import com.mredrock.cyxbs.network.interceptor.StudentNumberInterceptor;
import com.mredrock.cyxbs.network.observable.CourseListProvider;
import com.mredrock.cyxbs.network.service.LostApiService;
import com.mredrock.cyxbs.network.service.RedrockApiService;
import com.mredrock.cyxbs.network.setting.CacheProviders;
import com.mredrock.cyxbs.network.setting.QualifiedTypeConverterFactory;
import com.mredrock.cyxbs.ui.activity.lost.LostActivity;
import com.mredrock.cyxbs.ui.fragment.social.TopicFragment;
import com.mredrock.cyxbs.util.BitmapUtil;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import io.victoralbertos.jolyglot.JacksonSpeaker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public enum RequestManager {

    INSTANCE;

    private static final int DEFAULT_TIMEOUT = 30;
    private RedrockApiService redrockApiService;
    private LostApiService lostApiService;
    private CacheProviders cacheProviders;
    private OkHttpClient okHttpClient;

    RequestManager() {
        okHttpClient = configureOkHttp(new OkHttpClient.Builder());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.END_POINT_REDROCK)
                .client(okHttpClient)
                .addConverterFactory(new QualifiedTypeConverterFactory(
                        GsonConverterFactory.create(), SimpleXmlConverterFactory.create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        cacheProviders = new RxCache.Builder()
                .persistence(APP.getContext().getFilesDir(), new JacksonSpeaker())
                .using(CacheProviders.class);

        redrockApiService = retrofit.create(RedrockApiService.class);
        lostApiService = retrofit.create(LostApiService.class);
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
        builder.addInterceptor(new StudentNumberInterceptor());

        return builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Subscription checkUpdate(Subscriber<UpdateInfo> subscriber, int versionCode) {

        Observable<UpdateInfo> observable = redrockApiService.update()
                .map(new UpdateVerifyFunc(versionCode));

        return emitObservable(observable, subscriber);
    }

    public Subscription login(Subscriber<User> subscriber, String stuNum, String idNum) {
        Observable<User> observable = redrockApiService.verify(stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>())
                .zipWith(redrockApiService.getPersonInfo(stuNum, idNum)
                        .map(new RedrockApiWrapperFunc<>()), User::cloneFromUserInfo);

        return emitObservable(observable, subscriber);

    }

    public Subscription getNowWeek(Subscriber<Integer> subscriber, String stuNum, String idNum) {
        Observable<Integer> observable = redrockApiService.getCourse(stuNum, idNum, "0")

                .map(courseWrapper -> {
                    if (courseWrapper.status != Const.REDROCK_API_STATUS_SUCCESS) {
                        throw new RedrockApiException();
                    }
                    return Integer.parseInt(courseWrapper.nowWeek);
                });
        return emitObservable(observable, subscriber);
    }

    public Subscription getCourseList(Subscriber<List<Course>> subscriber, String stuNum, String idNum, int week, boolean update) {
//        Observable<List<Course>> observable = CourseListProvider.start(stuNum, idNum, update,false)
//                .map(new UserCourseFilterFunc(week));

        return getCourseList(subscriber, stuNum, idNum, week, update, false);
    }

    public Subscription getCourseList(Subscriber<List<Course>> subscriber, String stuNum, String idNum,
                                      int week, boolean update, boolean forceFetch) {
        Observable<List<Course>> observable = CourseListProvider.start(stuNum, idNum, update, forceFetch)
                .map(new UserCourseFilterFunc(week));

        return emitObservable(observable, subscriber);
    }

    public Subscription getRemindableList(Subscriber<List<Reminder>> subscriber, Context context, BaseRemindFunc remindFunc) {
        Observable<List<Reminder>> observable = CourseListProvider.start(APP.getUser(context).stuNum, APP.getUser(context).idNum, false, false)
                .map(new UserCourseFilterFunc(new SchoolCalendar()
                        .getWeekOfTerm()))
                .map(remindFunc);
        return emitObservable(observable, subscriber);
    }

    public Observable<List<Course>> getCourseList(String stuNum, String idNum) {
        return redrockApiService.getCourse(stuNum, idNum, "0").map(new RedrockApiWrapperFunc<>());
    }

    public List<Course> getCourseListSync(String stuNum, String idNum, boolean forceFetch) throws IOException {
        Response<Course.CourseWrapper> response = redrockApiService.getCourseCall(stuNum, idNum, "0", forceFetch).execute();
        return response.body().data;

    }

    public Subscription getMapOverlayImageUrl(Subscriber<String> subscriber, String name, String path) {
        Observable<String> observable = redrockApiService.getMapOverlayImageUrl(name, path)
                .map(wrapper -> {
                    if (wrapper.status != 204) {
                        throw new RedrockApiException(wrapper.info);
                    } else {
                        return wrapper.data;
                    }
                })
                .flatMap(urlList -> Observable.just(urlList.get(0)));


        return emitObservable(observable, subscriber);
    }

    public Subscription getShake(Subscriber<Shake> subscriber) {
        Observable<Shake> observable = redrockApiService.getShake()
                .map(new RedrockApiWrapperFunc<>());

        return emitObservable(observable, subscriber);
    }

    public Subscription getFoodList(Subscriber<List<Food>> subscriber, String page, String defaultIntro) {
        Observable<List<Food>> observable = redrockApiService.getFoodList(page)
                .map(new RedrockApiWrapperFunc<>())
                .filter(Utils::checkNotNullAndNotEmpty)
                .flatMap(foodList -> {
                    for (Food food : foodList) {
                        redrockApiService.getFoodDetail(food.id)
                                .map(new RedrockApiWrapperFunc<>())
                                .filter(foodDetail -> foodDetail != null)
                                .onErrorReturn(throwable -> {
                                    FoodDetail defaultFoodDetail = new FoodDetail();
                                    defaultFoodDetail.shop_content = defaultIntro;
                                    return defaultFoodDetail;
                                })
                                .doOnNext(foodDetail -> foodDetail.shop_content =
                                        foodDetail.shop_content
                                                .replaceAll("\t", "")
                                                .replaceAll("\r\n", ""))
                                .subscribe(foodDetail -> {
                                    food.introduction = foodDetail.shop_content;
                                });
                    }

                    return Observable.just(foodList);
                });

        return emitObservable(observable, subscriber);
    }


    public Subscription getFoodAndCommentList(Subscriber<FoodDetail> subscriber, String shopId, String page) {

        Observable<FoodDetail> observable = redrockApiService.getFoodDetail(shopId)
                .map(new RedrockApiWrapperFunc<>())
                .filter(foodDetail -> foodDetail != null)
                .doOnNext(foodDetail -> {
                    foodDetail.shop_content = foodDetail.shop_content.replaceAll("\t", "").replaceAll("\r\n", "");
                    foodDetail.shop_tel = foodDetail.shop_tel.trim();
                })
                .flatMap(foodDetail -> {
                    redrockApiService.getFoodComments(shopId, page)
                            .map(new RedrockApiWrapperFunc<>())
                            .filter(Utils::checkNotNullAndNotEmpty)
                            .onErrorReturn(throwable -> new ArrayList<>())
                            .flatMap(Observable::from)
                            .toSortedList()
                            .subscribe(foodCommentList -> {
                                foodDetail.foodComments = foodCommentList;
                            });
                    return Observable.just(foodDetail);
                });

        return emitObservable(observable, subscriber);
    }

    public Subscription getFood(Subscriber<FoodDetail> subscriber, String restaurantKey) {
        Observable<FoodDetail> observable = redrockApiService.getFoodDetail(restaurantKey)
                .map(new RedrockApiWrapperFunc<>());
        return emitObservable(observable, subscriber);
    }

    public Subscription sendCommentAndRefresh(Subscriber<List<FoodComment>> subscriber, String shopId, String userId, String userPassword, String content, String authorName) {
        Observable<RedrockApiWrapper<Object>> sendObservable = redrockApiService.sendFoodComment(shopId, userId, userPassword, content, authorName);
        Observable<List<FoodComment>> foodCommentObservable =
                redrockApiService.getFoodComments(shopId, "1").map(new RedrockApiWrapperFunc<>())
                        .filter(foodCommentList -> Utils.checkNotNullAndNotEmpty(foodCommentList))
                        .flatMap(Observable::from)
                        .toSortedList();
        Observable<List<FoodComment>> observable = Observable.zip(sendObservable, foodCommentObservable,
                (wrapper, foodCommentList) -> {
                    if (wrapper.status == Const.REDROCK_API_STATUS_SUCCESS) {
                        return foodCommentList;
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
                        .filter(Utils::checkNotNullAndNotEmpty)
                        .flatMap(Observable::from)
                        .toSortedList();

        return emitObservable(observable, subscriber);
    }

    public void getPublicCourse(Subscriber<List<Course>> subscriber,
                                List<String> stuNumList, String week) {
        Observable<List<Course>> observable = Observable.from(stuNumList)
                .flatMap(s -> redrockApiService.getCourse(s, "", week))
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }


    public void getStudent(Subscriber<List<com.mredrock.cyxbs.model.Student>> subscriber,
                           String stu) {
        Observable<List<com.mredrock.cyxbs.model.Student>> observable = redrockApiService.getStudent(stu)
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
        cacheProviders.getCachedGradeList(observable, new DynamicKey
                (stuNum), new EvictDynamicKey(update))
                .map(Reply::getData);
        emitObservable(observable, subscriber);
    }

    public void getExamList(Subscriber<List<Exam>> subscriber, String
            stu, boolean update) {
        Observable<List<Exam>> observable = redrockApiService.getExam(stu).map(
                examWapper -> examWapper.data);
        cacheProviders.getCachedExamList(observable, new DynamicKey(stu), new
                EvictDynamicKey(update))
                .map(Reply::getData);
        emitObservable(observable, subscriber);
    }

    public void getReExamList(Subscriber<List<Exam>> subscriber, String
            stu, boolean update) {
        Observable<List<Exam>> observable = redrockApiService.getReExam(stu).map(
                examWapper -> examWapper.data);
        cacheProviders.getCachedExamList(observable, new DynamicKey(stu), new
                EvictDynamicKey(update))
                .map(Reply::getData);
        emitObservable(observable, subscriber);
    }

    public void getAboutMeList(Subscriber<List<AboutMe>> subscriber, String stuNum, String idNum) {
        Observable<List<AboutMe>> observable = redrockApiService.getAboutMe(stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }


    public void getTrendDetail(Subscriber<List<HotNews>> subscriber, int type_id, String article_id) {
        List<HotNews> newsList = new ArrayList<>();
        Observable<List<HotNews>> observable = redrockApiService.getTrendDetail(type_id, article_id)
                .flatMap(bbddDetailWrapper -> Observable.from(bbddDetailWrapper.data))
                .map(bbddDetail -> {
                    HotNews news = new HotNews(bbddDetail);
                    newsList.add(news);
                    return newsList;
                });
        emitObservable(observable, subscriber);
    }

    public void getMyTrend(Subscriber<List<HotNews>> subscriber, String stuNum, String idNum) {
        List<HotNews> newsList = new ArrayList<>();
        Observable<List<HotNews>> observable = redrockApiService.searchTrends(stuNum, idNum)
                .flatMap(mDetailWrapper -> Observable.from(mDetailWrapper.data))
                .map(mDetail -> {
                    HotNews news = new HotNews(mDetail);
                    newsList.add(news);
                    return newsList;
                })
                .map(hotNewses -> {
                    for (HotNews h : hotNewses) {
                        h.data.nickName = APP.getUser(APP.getContext()).getNickname();
                        h.data.userHead = APP.getUser(APP.getContext()).photo_thumbnail_src;
                    }
                    return hotNewses;
                });
        emitObservable(observable, subscriber);
    }

    /**
     * api
     */
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

    public void getHotArticle(Subscriber<List<HotNews>> subscriber, int size, int page) {
        Observable<List<HotNews>> observable = redrockApiService.getSocialHotList(size, page);
        emitObservable(observable, subscriber);
    }

    public void getListNews(Subscriber<List<HotNews>> subscriber, int size, int page) {
        Observable<List<HotNews>> observable = redrockApiService.getSocialOfficialNewsList(size, page)
                .map(new RedrockApiWrapperFunc<>())
                .map(officeNewsContentList -> {
                    List<HotNews> aNews = new ArrayList<>();
                    for (OfficeNewsContent officeNewsContent : officeNewsContentList)
                        aNews.add(new HotNews(officeNewsContent));
                    return aNews;
                });
        emitObservable(observable, subscriber);
    }

    public void getListArticle(Subscriber<List<HotNews>> subscriber, int type_id, int size, int page) {
        Observable<List<HotNews>> observable = redrockApiService.getSocialBBDDList(type_id, size, page)
                .map(new RedrockApiWrapperFunc<>())
                .flatMap(bbdd -> Observable.just(bbdd)
                        .map(mBBDD -> {
                            List<HotNews> aNews = new ArrayList<>();
                            for (BBDDNewsContent bbddNewsContent : mBBDD)
                                aNews.add(new HotNews(bbddNewsContent));
                            return aNews;
                        }));
        emitObservable(observable, subscriber);

    }

    public Observable<String> sendDynamic(int type_id,
                                          String title,
                                          String content,
                                          String thumbnail_src,
                                          String photo_src,
                                          String user_id,
                                          String stuNum,
                                          String idNum) {
        if (!checkWithUserId("没有完善信息,还想发动态？")) return null;
        return redrockApiService.sendDynamic(type_id, title, user_id, content, thumbnail_src, photo_src, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public void getRemarks(Subscriber<List<CommentContent>> subscriber,
                           String article_id,
                           int type_id) {
        Observable<List<CommentContent>> observable = redrockApiService.getSocialCommentList(article_id, type_id)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }


    public void postReMarks(Subscriber<String> subscriber,
                            String article_id,
                            int type_id,
                            String content,
                            String user_id,
                            String stuNum,
                            String idNum) {
        if (!checkWithUserId("没有完善信息,还想发回复？")) return;
        Observable<String> observable = redrockApiService.addSocialComment(article_id, type_id, content, user_id, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }

    public void addThumbsUp(Subscriber<String> subscriber,
                            String article_id,
                            int type_id,
                            String stuNum,
                            String idNum) {
        if (!checkWithUserId("没有完善信息,肯定不让你点赞呀")) return;
        Observable<String> observable = redrockApiService.socialLike(article_id, type_id, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }

    public void cancelThumbsUp(Subscriber<String> subscriber,
                               String article_id,
                               int type_id,
                               String stuNum,
                               String idNum) {
        if (!checkWithUserId("没有完善信息,肯定不让你点赞呀")) return;
        Observable<String> observable = redrockApiService.socialUnlike(article_id, type_id, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
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


    public void getPersonInfo(Subscriber<PersonInfo> subscriber, String otherStuNum, String stuNum, String idNum) {
        Observable<PersonInfo> observable = redrockApiService.getPersonInfo(otherStuNum, stuNum, idNum)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }

    public void getPersonLatestList(Subscriber<List<HotNews>> subscriber, String otherStuNum, String userName, String userHead) {
        Observable<List<HotNews>> observable = redrockApiService.getPersonLatestList(otherStuNum)
                .map(new RedrockApiWrapperFunc<>())
                .map(latestsList -> {
                    List<HotNews> aNews = new ArrayList<>();
                    for (PersonLatest personLatest : latestsList)
                        aNews.add(new HotNews(personLatest, otherStuNum, userName, userHead));
                    return aNews;
                });
        emitObservable(observable, subscriber);
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

    public void getAffair(Subscriber<List<Affair>> subscriber, String stuNum, String idNum) {
        Observable<List<Affair>> observable = redrockApiService.getAffair(stuNum, idNum)
                .map(new AffairTransformFunc());
        emitObservable(observable, subscriber);
    }

    public void getAffair(Subscriber<List<Affair>> subscriber, String stuNum, String idNum, int week) {
        Observable<List<Affair>> observable = redrockApiService.getAffair(stuNum, idNum)
                .map(new AffairTransformFunc())
                .map(new AffairWeekFilterFunc(week));
        emitObservable(observable, subscriber);
    }

    public void addAffair(Subscriber<Object> subscriber, String stuNum, String idNum, String uid, String title,
                          String content, String date, int time) {
        Observable<Object> observable = redrockApiService.addAffair(uid, stuNum, idNum, date, time, title, content)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }


    public void editAffair(Subscriber<Object> subscriber, String stuNum, String idNum, String uid, String title,
                           String content, String date, int time) {
        Observable<Object> observable = redrockApiService.editAffair(uid, stuNum, idNum, date, time, title, content)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }

    public void deleteAffair(Subscriber<Object> subscriber, String stuNum, String idNum, String uid) {
        Observable<Object> observable = redrockApiService.deleteAffair(stuNum, idNum, uid)
                .map(new RedrockApiWrapperFunc<>());
        emitObservable(observable, subscriber);
    }

    public void getStartPage(Subscriber<StartPage> subscriber) {
        Observable<StartPage> observable = redrockApiService.startPage()
                .map(new RedrockApiWrapperFunc<>())
                .map(new StartPageFunc());
        emitObservable(observable, subscriber);
    }

    public void queryElectricCharge(Subscriber<ElectricCharge> subscriber, String building, String room) {
        if (!checkWithUserId("需要先登录才能发送失物招领信息哦")) return;
        Observable<ElectricCharge> observable = redrockApiService.queryElectricCharge(building, room)

                .map(new ElectricQueryFunc());
        emitObservable(observable, subscriber);
    }

    public void getLostList(Subscriber<LostWrapper<List<Lost>>> subscriber, int theme, String category, int page) {
        String themeString;
        if (theme == LostActivity.THEME_LOST) {
            themeString = "lost";
        } else {
            themeString = "found";
        }
        Observable<LostWrapper<List<Lost>>> observable = lostApiService.getLostList(themeString, category, page);
        emitObservable(observable, subscriber);
    }

    public void getLostDetail(Subscriber<LostDetail> subscriber, Lost origin) {
        Observable<LostDetail> observable = lostApiService.getLostDetial(origin.id)
                .map(lostDetail -> lostDetail.mergeLost(origin));
        emitObservable(observable, subscriber);
    }

    public void createLost(Subscriber<LostStatus> subscriber, LostDetail detail, int theme) {
        if (!checkWithUserId("需要先登录才能发送失物招领信息哦")) return;
        User user = APP.getUser(APP.getContext());
        String themeString;
        if (theme == LostActivity.THEME_LOST) {
            themeString = "寻物启事";
        } else {
            themeString = "失物招领";
        }
        Observable<LostStatus> observable = lostApiService.create(user.stuNum,
                user.idNum,
                themeString,
                detail.category,
                detail.description,
                detail.time,
                detail.place,
                detail.connectPhone,
                detail.connectWx);
        emitObservable(observable, subscriber);
    }

    public void getTopicList(Subscriber<List<Topic>> subscriber, int size, int page, String stuNum, String idNum, String type) {
        Observable<List<Topic>> observable;
        switch (type) {
            case TopicFragment.TopicType.MY_TOPIC:
                observable = redrockApiService.getMyTopicList(stuNum, idNum, size, page)
                        .map(new RedrockApiWrapperFunc<>());
                break;
            case TopicFragment.TopicType.ALL_TOPIC:
                observable = redrockApiService.getAllTopicList(stuNum, idNum, size, page)
                        .map(new RedrockApiWrapperFunc<>());
                break;
            default:
                observable = redrockApiService.searchTopic(stuNum, idNum, size, page, type)
                        .map(new RedrockApiWrapperFunc<>());
                break;
        }
        emitObservable(observable, subscriber);
    }

    private <T> Subscription emitObservable(Observable<T> o, Subscriber<T> s) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public boolean checkWithUserId(String s) {
        if (!APP.isLogin()) {
            EventBus.getDefault().post(new AskLoginEvent(s));
            return false;
        } else {
            return true;
        }

    }
}

