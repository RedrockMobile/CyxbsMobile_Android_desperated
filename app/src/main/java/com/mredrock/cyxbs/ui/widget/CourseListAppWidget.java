package com.mredrock.cyxbs.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.ui.activity.ActionActivity;
import com.mredrock.cyxbs.ui.activity.MainActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Course List App Widget Provider
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class CourseListAppWidget extends AppWidgetProvider {
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


        Intent itemClickIntent = new Intent(context, ActionActivity.class);
        itemClickIntent.setAction(context.getResources().getString(R.string.action_appwidget_item_on_click));
        views.setPendingIntentTemplate(R.id.lv_app_widget_course_list, PendingIntent.getActivity(context, 0, itemClickIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        User user = BaseAPP.getUser(context);
        if (user == null) {
            Log.i(getClass().getName(), "onUpdate: can not get user");
            return;
        }
        CourseListAppWidgetUpdateWorker.startSingleWork(user.stuNum, user.id, true, 0);
        CourseListAppWidgetUpdateWorker.startSingleWork(user.stuNum, user.idNum, true, getTomorrowTimeInMillis());
        // refresh app widget
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        User user = BaseAPP.getUser(context);
        if (user == null) {
            Log.i(getClass().getName(), "onEnabled: can not get user");
            return;
        }
        CourseListAppWidgetUpdateWorker.startPeriodicWork(user.stuNum, user.idNum, true);
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
        CourseListAppWidgetUpdateWorker.cancel();
    }

    public static void updateNow(Context context) {
        Intent intent = new Intent(context, CourseListAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, CourseListAppWidget.class)));
        context.sendBroadcast(intent);
    }

}

