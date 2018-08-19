package com.mredrock.cyxbs.network.observable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.util.FileUtils;
import com.mredrock.cyxbs.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Get Course List From Network or Cache
 * <p>
 * Course List will be cached automatically in {@link #getCourseFromNetwork()} if there is no exception be threw.
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CourseListProvider implements ObservableOnSubscribe<List<Course>> {

    private String stuNum;
    private String idNum;
    private boolean preferRefresh;
    private String cacheFilePath;
    private boolean forceFetch;

    /**
     * Get course list, we will try network and cache until one of them success or both in failure.
     *
     * @param stuNum        student number
     * @param idNum         id number
     * @param preferRefresh if true, we will try network first, or try cache first
     * @return RxJava Observable
     */
    public static Observable<List<Course>> start(String stuNum, String idNum, boolean preferRefresh, boolean forceFetch) {
        return Observable.create(new CourseListProvider(stuNum, idNum, preferRefresh, forceFetch));
    }

    /**
     * sync method of call for {@link com.mredrock.cyxbs.ui.widget.CourseListRemoteViewsService}
     *
     * @param stuNum        student number
     * @param idNum         id number
     * @param preferRefresh if true, we will try network first, or try cache first
     * @return a list of course
     */
    public static List<Course> exec(String stuNum, String idNum, boolean preferRefresh, boolean forceFetch) {
        return new CourseListProvider(stuNum, idNum, preferRefresh, forceFetch).doubleTryLoadSync();
    }

    private CourseListProvider(String stuNum, String idNum, boolean preferRefresh, boolean forceFetch) {
        this.stuNum = stuNum;
        this.idNum = idNum;
        this.preferRefresh = preferRefresh;
        this.forceFetch = forceFetch;
        cacheFilePath = BaseAPP.getContext().getFilesDir().getAbsolutePath() + "/" + "UserCourse$" + stuNum + ".json";
    }

    @Override
    public void subscribe(ObservableEmitter<List<Course>> e) throws Exception {
        doubleTryLoad(e);
    }

    private List<Course> doubleTryLoadSync() {
        try {
            if (preferRefresh) {
                return getCourseFromNetwork();
            } else {
                return getCourseFromCache();
            }
        } catch (Throwable e) {
            LogUtils.LOGW("CourseProviderObservable", "Ignored " + (preferRefresh ? "NetworkError" : "CacheError"), e);
            try {
                if (preferRefresh) {
                    return getCourseFromCache();
                } else {
                    return getCourseFromNetwork();
                }
            } catch (Throwable ex) {
                LogUtils.LOGE("CourseProviderObservable", preferRefresh ? "NetworkError" : "CacheError", e);
                LogUtils.LOGE("CourseProviderObservable", preferRefresh ? "CacheError" : "NetworkError", ex);
                throw new RuntimeException(ex);
            }
        }
    }

    private void doubleTryLoad(ObservableEmitter<? super List<Course>> emitter) {
        try {
            if (preferRefresh) {
                emitter.onNext(getCourseFromNetwork());
            } else {
                emitter.onNext(getCourseFromCache());
            }
            emitter.onComplete();
        } catch (Throwable e) {
            try {
                if (preferRefresh) {
                    emitter.onNext(getCourseFromCache());
                } else {
                    emitter.onNext(getCourseFromNetwork());
                }
                emitter.onComplete();
            } catch (Throwable ex) {
                emitter.onError(new ConnectException());
                LogUtils.LOGE("CourseProviderObservable", preferRefresh ? "NetworkError" : "CacheError", e);
                LogUtils.LOGE("CourseProviderObservable", preferRefresh ? "CacheError" : "NetworkError", ex);
            }
        }
    }

    private List<Course> getCourseFromNetwork() throws IOException {
        LogUtils.LOGI("CourseProviderObservable", "onGetFromNetwork");
        List<Course> courses = RequestManager.getInstance().getCourseListSync(stuNum, idNum, forceFetch);
        if (courses == null || courses.size() == 0) {
            throw new NullPointerException();
        }
        cacheCourseList(courses);
        return courses;
    }

    private List<Course> getCourseFromCache() {
        LogUtils.LOGI("CourseProviderObservable", "onGetFromCache");
        String json = FileUtils.readStringFromFile(new File(cacheFilePath));
        List<Course> courses = new Gson().fromJson(json, new TypeToken<List<Course>>() {
        }.getType());
        if (courses == null || courses.size() == 0) {
            throw new NullPointerException();
        } else {
            return courses;
        }
    }

    private void cacheCourseList(List<Course> courses) {
        LogUtils.LOGI("CourseProviderObservable", "onCacheCourseList");
        FileUtils.writeStringToFile(new Gson().toJson(courses), new File(cacheFilePath));
    }
}
