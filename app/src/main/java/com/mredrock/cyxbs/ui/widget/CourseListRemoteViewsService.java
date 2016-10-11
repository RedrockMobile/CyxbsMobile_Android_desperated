package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.func.UserCourseFilterByWeekDayFunc;
import com.mredrock.cyxbs.network.func.UserCourseFilterFunc;
import com.mredrock.cyxbs.network.observable.CourseListProvider;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CourseListRemoteViewsService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (!APP.isLogin()) {
            return new ErrorFactory(getApplicationContext(), ErrorFactory.ERROR_TYPE_UN_LOGIN);
        }
        List<Course> data;
        try {
            data = CourseListProvider.exec(APP.getUser(APP.getContext()).stuNum, APP.getUser(APP.getContext()).idNum, false);
            data = new UserCourseFilterFunc(new SchoolCalendar().getWeekOfTerm()).call(data);
            data = new UserCourseFilterByWeekDayFunc(new GregorianCalendar().get(Calendar.DAY_OF_WEEK)).call(data);
        } catch (Exception ignored) {
            return new ErrorFactory(getApplicationContext(), ErrorFactory.ERROR_TYPE_NETWORK);
        }
        if (data == null || data.size() == 0) {
            return new ErrorFactory(getApplicationContext(), ErrorFactory.ERROR_TYPE_NO_COURSE);
        }
        return new Factory(getApplicationContext(), intent, data);
    }

    private class Factory implements RemoteViewsService.RemoteViewsFactory {

        Context context;
        Intent intent;
        SparseArray<Item> items = new SparseArray<>(12);

        Factory(Context context, Intent intent, List<Course> data) {
            this.context = context;
            this.intent = intent;
            generateItem(data);
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

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Item item = items.valueAt(position);
            RemoteViews views;
            if (item.getText() == null) {  // empty course
                Log.d("getViewAt", "Position: " + position + ", Empty: " + item.getOrderString());
                views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item_empty);
                views.setTextViewText(R.id.tv_app_widget_course_item_order, item.getOrderString());
            } else {
                Log.d("getViewAt", "Position: " + position + ", Normal: " + item.getOrderString() + ", \n" + item.getText());
                views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item);
                views.setTextViewText(R.id.tv_app_widget_course_item_order, item.getOrderString());
                views.setTextViewText(R.id.tv_app_widget_course_item_content, item.getText());
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
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

    private class ErrorFactory implements RemoteViewsFactory {

        Context context;
        private int errorType = -1;
        private String tip = null;

        public static final int ERROR_TYPE_UN_LOGIN = 32001;
        public static final int ERROR_TYPE_NETWORK = 32002;
        public static final int ERROR_TYPE_NO_COURSE = 32003;

        ErrorFactory(Context context, int errorType) {
            this.context = context;
            this.errorType = errorType;
        }

        ErrorFactory(Context context, String tip) {
            this.context = context;
            this.tip = tip;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            switch (errorType) {
                case ERROR_TYPE_NETWORK:
                    tip = "获取课表失败";
                    break;
                case ERROR_TYPE_NO_COURSE:
                    tip = "今天没有课";
                    break;
                case ERROR_TYPE_UN_LOGIN:
                    tip = "请先登录";
                    break;
            }
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item_error);
            views.setTextViewText(R.id.tv_app_widget_course_item_error, tip);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
