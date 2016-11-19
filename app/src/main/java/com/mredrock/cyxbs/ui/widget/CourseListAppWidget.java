package com.mredrock.cyxbs.ui.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.MainActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Course List App Widget Provider
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CourseListAppWidget extends AppWidgetProvider {

    PendingIntent updatePendingIntent;
    public static final String EXTRA_COURSES = "appwidget_extra_courses";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d("AppWidget", "updateAppWidget");

        RemoteViews views = generateRemoteViews(context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_app_widget_course_list);
    }

    public static RemoteViews generateRemoteViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list);

        Intent mainIntent = new Intent(context, MainActivity.class);
        views.setOnClickPendingIntent(R.id.tv_app_widget_title, PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        Intent serviceIntent = new Intent(context, CourseListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lv_app_widget_course_list, serviceIntent);


        Intent itemClickIntent = new Intent(context, MainActivity.class);
        itemClickIntent.setAction(context.getResources().getString(R.string.action_appwidget_item_on_click));
        views.setPendingIntentTemplate(R.id.lv_app_widget_course_list, PendingIntent.getActivity(context, 0, itemClickIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // set alarm first
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (updatePendingIntent == null) {
            Intent alarmIntent = new Intent(context, CourseListAppWidgetUpdateService.class);
            alarmIntent.putExtra(CourseListAppWidgetUpdateService.EXTRA_UPDATE, true);
            updatePendingIntent = PendingIntent.getService(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, getTomorrowTimeInMillis(), updatePendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC, getTomorrowTimeInMillis(), updatePendingIntent);
        }
        // refresh app widget
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        CourseListAppWidgetUpdateService.start(context, true);
    }

    private long getTomorrowTimeInMillis() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar = new GregorianCalendar(year, month, day, 0, 0, 1);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onDisabled(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (updatePendingIntent == null) {
            Intent alarmIntent = new Intent(context, CourseListAppWidgetUpdateService.class);
            updatePendingIntent = PendingIntent.getService(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.cancel(updatePendingIntent);
    }

    public static void updateNow(Context context) {
        Intent intent = new Intent(context, CourseListAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, CourseListAppWidget.class)));
        context.sendBroadcast(intent);
    }

}

