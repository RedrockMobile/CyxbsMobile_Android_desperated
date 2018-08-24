package com.mredrock.cyxbs.ui.widget;

import android.os.Build;
import android.support.annotation.NonNull;

import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.func.AppWidgetCacheAndUpdateFunc;
import com.mredrock.cyxbs.network.observable.CourseListProvider;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.database.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import io.reactivex.schedulers.Schedulers;

public class CourseListAppWidgetUpdateWorker extends Worker {

    public static final String EXTRA_UPDATE = "updateFromNetwork";
    public static final String EXTRA_STU_NUM = "stuNum";
    public static final String EXTRA_ID_NUM = "idNum";

    public static final String TAG = CourseListAppWidgetUpdateWorker.class.getName();

    @NonNull
    @Override
    public Result doWork() {
        load(getInputData());
        return Result.SUCCESS;
    }

    private void load(Data data) {
        String stuNum = data.getString(EXTRA_STU_NUM);
        String idNum = data.getString(EXTRA_ID_NUM);
        boolean updateFromNetwork = data.getBoolean(EXTRA_UPDATE, true);

        if (stuNum == null || idNum == null) {
            LogUtils.LOGI(getClass().getName(), "not login stop update.");
            return;
        }

        DBManager dbManager = DBManager.INSTANCE;
        dbManager.query(stuNum, new SchoolCalendar().getWeekOfTerm())
                .zipWith(CourseListProvider.start(stuNum, idNum, updateFromNetwork, false), (courses, courses2) -> {
                    if (courses == null) courses = new ArrayList<>();
                    if (courses2 == null) courses2 = new ArrayList<>();
                    courses.addAll(courses2);
                    return courses;
                })
                .map(new AppWidgetCacheAndUpdateFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new SimpleObserver<>(getApplicationContext(), false, false, new SubscriberListener<List<Course>>() {
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

    public static void startPeriodicWork(String stuNum, String idNum, boolean updateFromNetwork) {
        Data data = new Data.Builder()
                .putString(EXTRA_STU_NUM, stuNum)
                .putString(EXTRA_ID_NUM, idNum)
                .putBoolean(EXTRA_UPDATE, updateFromNetwork)
                .build();
        Constraints.Builder constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraints.setRequiresDeviceIdle(true);
        }
        WorkRequest request = new PeriodicWorkRequest.Builder(
                CourseListAppWidgetUpdateWorker.class, 1L, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(constraints.build())
                .addTag(TAG)
                .build();
        WorkManager.getInstance().enqueue(request);
    }

    public static void startSingleWork(String stuNum, String idNum, boolean updateFromNetwork, long delay) {
        Data data = new Data.Builder()
                .putString(EXTRA_STU_NUM, stuNum)
                .putString(EXTRA_ID_NUM, idNum)
                .putBoolean(EXTRA_UPDATE, updateFromNetwork)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(CourseListAppWidgetUpdateWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(TAG)
                .build();
        WorkManager.getInstance().beginUniqueWork(TAG + delay, ExistingWorkPolicy.REPLACE, request);
    }

    public static void cancel() {
        WorkManager.getInstance().cancelAllWorkByTag(TAG);
    }
}
