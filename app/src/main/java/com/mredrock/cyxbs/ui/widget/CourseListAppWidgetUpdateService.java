package com.mredrock.cyxbs.ui.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.RequestManager;

import java.util.List;

import rx.Subscriber;

public class CourseListAppWidgetUpdateService extends Service {
    public CourseListAppWidgetUpdateService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RequestManager.getInstance().getCourseList(new Subscriber<List<Course>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Course> courses) {

            }
        }, APP.getUser(APP.getContext()).stuNum, APP.getUser(APP.getContext()).idNum, 0, false);
        return super.onStartCommand(intent, flags, startId);
    }
}
