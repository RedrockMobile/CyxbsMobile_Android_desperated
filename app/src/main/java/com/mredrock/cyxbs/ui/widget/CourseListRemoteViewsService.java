package com.mredrock.cyxbs.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import rx.Subscriber;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CourseListRemoteViewsService extends RemoteViewsService {

    public static final String ACTION_NOTIFY_DATA_CHANGED = "com.mredrock.cyxbs.ui.widget.ACTION_NOTIFY_DATA_CHANGED";

    Factory factory;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("RemoteFactory", "Service onGetViewFactory");
        factory = new Factory(this, intent);
        return factory;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("RemoteFactory", "ServiceCreate, " + (factory != null));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("RemoteFactory", "ServiceDestroy, " + (factory != null));
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_NOTIFY_DATA_CHANGED)) {
            return binder;
        }
        return super.onBind(intent);
    }

    public class Binder extends android.os.Binder {
        public void refresh() {
            if (factory != null) {
                factory.refresh();
            }
        }
    }

    public final Binder binder = new Binder();

    private class Factory implements RemoteViewsService.RemoteViewsFactory {

        Context context;
        Intent intent;
        SparseArray<Item> items = new SparseArray<>(12);
        int factoryType;
        ArrayList<RemoteViews> views = new ArrayList<>(12);

        static final int FACTORY_TYPE_NORMAL = 0;
        static final int FACTORY_TYPE_ERROR = -1;
        static final int FACTORY_TYPE_LOADING = -2;

        Factory(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
        }

        private void refresh() {
            items.clear();
            if (!APP.isLogin()) {
                setError("请先登录");
                return;
            }
            setLoading();
            RequestManager.getInstance().getCourseList(new Subscriber<List<Course>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    LogUtils.LOGE("RemoteFactory", "refresh onError", e);
                    setError("获取课表失败");
                }

                @Override
                public void onNext(List<Course> courses) {
                    if (courses == null || courses.size() == 0) {
                        setError("今天没有课");
                        return;
                    }
                    generateItem(courses);
                    setNormal();
                }
            }, APP.getUser(context).stuNum, APP.getUser(context).idNum,
                    new SchoolCalendar().getWeekOfTerm(),
                    new GregorianCalendar().get(Calendar.DAY_OF_WEEK), false);
        }

        private void setError(String message) {
            Log.i("RemoteFactory", "setError: " + message);
            this.factoryType = FACTORY_TYPE_ERROR;
            this.views.clear();
            RemoteViews errorViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item_error);
            errorViews.setTextViewText(R.id.tv_app_widget_course_item_error, message);
            this.views.add(errorViews);
            notifyDataSetChanged();
        }

        private void setNormal() {
            Log.i("RemoteFactory", "setNormal");
            this.factoryType = FACTORY_TYPE_NORMAL;
            this.views.clear();
            for (int i = 0; i < items.size(); i++) {
                RemoteViews views;
                Item item = items.valueAt(i);
                if (item.getText() == null) {  // empty course
                    Log.d("getViewAt", "Position: " + i + ", Empty: " + item.getOrderString());
                    views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item_empty);
                    views.setTextViewText(R.id.tv_app_widget_course_item_order, item.getOrderString());
                } else {
                    Log.d("getViewAt", "Position: " + i + ", Normal: " + item.getOrderString() + ", \n" + item.getText());
                    views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item);
                    views.setTextViewText(R.id.tv_app_widget_course_item_order, item.getOrderString());
                    views.setTextViewText(R.id.tv_app_widget_course_item_content, item.getText());
                }
                this.views.add(views);
            }
            notifyDataSetChanged();
        }

        private void setLoading() {
            Log.i("RemoteFactory", "setLoading");
            this.factoryType = FACTORY_TYPE_LOADING;
            this.views.clear();
            RemoteViews loadingViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item_error);
            loadingViews.setTextViewText(R.id.tv_app_widget_course_item_error, "正在加载中...");
            this.views.add(loadingViews);
            notifyDataSetChanged();
        }

        private void generateItem(@Nullable List<Course> data) {
            if (data != null) {
                for (Course c : data) {
                    if (items.indexOfKey(c.begin_lesson) < 0) {  // fill course
                        items.put(c.begin_lesson, new Item(c));
                    } else {  // merge course
                        items.put(c.begin_lesson, items.get(c.begin_lesson).addCourse(c));
                    }
                }
            }
            // find empty course
            boolean[] hasCourses = new boolean[13];  // unused hasCourse[0] to start with 1
            for (int i = 1; i < 13; i++) {
                if (items.indexOfKey(i) < 0) continue;
                Item item = items.get(i);
                for (int j = item.start; j < item.end; j++) {
                    hasCourses[j] = true;
                }
            }
            // fill empty
            for (int i = 1; i < 13; i++) {
                if (!hasCourses[i]) {
                    items.put(i, new Item(i));
                }
            }
            Log.v("finalItems", items.toString());
        }

        @Override
        public void onCreate() {
            Log.d("RemoteFactory", "onCreate");
            refresh();
        }

        @Override
        public void onDataSetChanged() {
            Log.d("RemoteFactory", "onDataSetChanged");
            // Do not call {@link #refresh()} here because it will cause a recursive call
            // If you want to refresh data, call {@link #refresh()}
            // If you changed views and want to reload it, call {@link #notifyDataSetChanged()}
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return this.views.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            return this.views.get(position);
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        public void notifyDataSetChanged() {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, CourseListAppWidget.class));
            manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_app_widget_course_list);
        }

        private class Item {

            Item(Course course) {
                start = course.begin_lesson;
                end = course.begin_lesson + course.period;
                text = course.toCourseString();
            }

            Item addCourse(Course course) {
                if (start != course.begin_lesson) throw new IllegalArgumentException("Can't merge two course which have different start time");
                if (end < start + course.period) {
                    end = start + course.period;
                }
                text += "，" + course.toCourseString();
                return this;
            }

            Item(int start) {
                this.start = start;
                this.end = start + 1;
                this.text = null;
            }

            String getText() {
                Log.v("Item", "getText: " + text);
                return text;
            }

            String getOrderString() {
                StringBuilder builder = new StringBuilder(start + "");
                for (int i = start + 1; i < end; i++) {
                    builder.append('\n').append(i);
                }
                Log.v("Item", "getText: " + builder.toString());
                return builder.toString();
            }

            @Override
            public String toString() {
                return "Item{" +
                        "start=" + start +
                        ", end=" + end +
                        ", text='" + text + '\'' +
                        '}';
            }

            private int start;  // 三四节的 start 是 3
            private int end;  // 三四节课的 end 是 5
            private String text;
        }

    }

}
