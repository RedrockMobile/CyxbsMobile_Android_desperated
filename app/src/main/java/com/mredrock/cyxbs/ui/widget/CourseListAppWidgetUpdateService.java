package com.mredrock.cyxbs.ui.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.func.AppWidgetCacheAndUpdateFunc;
import com.mredrock.cyxbs.network.observable.CourseListProvider;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.database.DBManager;

import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

public class CourseListAppWidgetUpdateService extends Service {

    public static final String EXTRA_UPDATE = "update";

    public CourseListAppWidgetUpdateService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        load(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void load(Intent intent){
        if (!APP.isLogin()) {
            LogUtils.LOGI("AppWidgetUpdateService", "not login, stop here");
            return;
        }
        boolean update = true;
        try {
            update = intent.getBooleanExtra(EXTRA_UPDATE, true);
        } catch (Exception e) {
            LogUtils.LOGW("AppWidgetUpdateService", "can't get extra", e);
        }
        DBManager dbManager = DBManager.INSTANCE;
        dbManager.query(APP.getUser(getApplicationContext()).stuNum, new SchoolCalendar().getWeekOfTerm())
                .zipWith(CourseListProvider.start(APP.getUser(APP.getContext()).stuNum, "", update), (courses, courses2) -> {
                    if (courses == null) courses = new ArrayList<>();
                    if (courses2 == null) courses2 = new ArrayList<>();
                    courses.addAll(courses2);
                    return courses;
                })
                .map(new AppWidgetCacheAndUpdateFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new SimpleSubscriber<List<Course>>(getApplicationContext(), false, false, new SubscriberListener<List<Course>>() {
                    @Override
                    public void onNext(List<Course> affairs) {
                        LogUtils.LOGD("UpdateSuccess", affairs.toString());
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        LogUtils.LOGE("AppWidgetUpdateService", "load: onError", e);
                        return true;
                    }
                }));
    }

    public static void start(Context context, boolean updateFromNetwork) {
        Intent starter = new Intent(context, CourseListAppWidgetUpdateService.class);
        starter.putExtra(EXTRA_UPDATE, updateFromNetwork);
        context.startService(starter);
    }

}
