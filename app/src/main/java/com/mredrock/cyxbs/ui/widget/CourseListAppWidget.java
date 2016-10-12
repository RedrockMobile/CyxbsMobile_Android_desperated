package com.mredrock.cyxbs.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.LogUtils;

/**
 * Course List App Widget Provider
 */
public class CourseListAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d("AppWidget", "updateAppWidget");

        RemoteViews views = generateRemoteViews(context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static RemoteViews generateRemoteViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list);

        Intent serviceIntent = new Intent(context, CourseListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lv_app_widget_course_list, serviceIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateNow(Context context) {
        Log.d("AppWidget", "updateNow");
        Intent serviceIntent = new Intent(APP.getContext(), CourseListRemoteViewsService.class);
        serviceIntent.setAction(CourseListRemoteViewsService.ACTION_NOTIFY_DATA_CHANGED);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    CourseListRemoteViewsService.Binder binder = (CourseListRemoteViewsService.Binder) service;
                    binder.refresh();
                    LogUtils.LOGI("AppWidget", "updateNow onServiceConnected finish");
                } catch (Throwable t) {
                    LogUtils.LOGE("AppWidget", "updateNow onServiceConnected", t);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        Intent intent = new Intent(context, CourseListAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, CourseListAppWidget.class)));
        context.sendBroadcast(intent);
    }

}

