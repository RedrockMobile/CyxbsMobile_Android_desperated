package com.mredrock.cyxbs.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.mredrock.cyxbs.R;

/**
 * Course List App Widget Provider
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CourseListAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d("AppWidget", "updateAppWidget");

        RemoteViews views = generateRemoteViews(context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_app_widget_course_list);
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
        // TODO: register a alarm clock here for auto refresh in 0:00
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        // TODO: unregister the alarm clock which register in onEnable
    }

    public static void updateNow(Context context) {
        Intent intent = new Intent(context, CourseListAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, CourseListAppWidget.class)));
        context.sendBroadcast(intent);
    }

}

