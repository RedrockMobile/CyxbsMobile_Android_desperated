package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ScheduleView;
import com.mredrock.cyxbs.config.Config;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ListView Remote Adapter of {@link com.mredrock.cyxbs.R.id#lv_app_widget_course_list} in {@link CourseListAppWidget}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CourseListRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("RemoteFactory", "Service onGetViewFactory");
        return new Factory(this, intent);
    }

    private class Factory implements RemoteViewsService.RemoteViewsFactory {

        Context context;
        Intent intent;
        SparseArray<Item> items = new SparseArray<>(12);
        int factoryType;
        ArrayList<RemoteViews> views = new ArrayList<>(12);
        ScheduleView.CourseColorSelector colorSelector = new ScheduleView.CourseColorSelector();

        static final int FACTORY_TYPE_NORMAL = 0;
        static final int FACTORY_TYPE_ERROR = -1;
        static final int FACTORY_TYPE_LOADING = -2;

        Factory(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
        }

        private void refresh() {
            items.clear();
            setLoading();
            List<Course> courses;
            try {
                courses = getCourseList();
            } catch (Exception e) {
                courses = null;
            }
            if (courses == null || courses.size() == 0) {
                setError("今天没有课");
                return;
            }
            for (Course c: courses) {
                colorSelector.addCourse(c.course);
            }
            generateItem(courses);
            setNormal();
        }

        private void setError(String message) {
            Log.i("RemoteFactory", "setError: " + message);
            this.factoryType = FACTORY_TYPE_ERROR;
            this.views.clear();
            RemoteViews errorViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item_error);
            errorViews.setTextViewText(R.id.tv_app_widget_course_item_error, message);
            this.views.add(errorViews);
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
                    views.setInt(R.id.tv_app_widget_course_item_content, "setBackgroundColor", colorSelector.getCourseColor(item.getCourses().get(0).course));
                    Intent coursesIntent = new Intent();
                    coursesIntent.putParcelableArrayListExtra(CourseListAppWidget.EXTRA_COURSES, item.getCourses());
                    views.setOnClickFillInIntent(R.id.tv_app_widget_course_item_content, coursesIntent);
                }
                this.views.add(views);
            }
        }

        private void setLoading() {
            Log.i("RemoteFactory", "setLoading");
            this.factoryType = FACTORY_TYPE_LOADING;
            this.views.clear();
            RemoteViews loadingViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item_error);
            loadingViews.setTextViewText(R.id.tv_app_widget_course_item_error, "正在加载中...");
            this.views.add(loadingViews);
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

        private List<Course> getCourseList() {
            String json = FileUtils.readStringFromFile(new File(APP.getContext().getFilesDir().getAbsolutePath() + "/" + Config.APP_WIDGET_CACHE_FILE_NAME));
            return new Gson().fromJson(json, new TypeToken<List<Course>>() {}.getType());
        }

        @Override
        public void onCreate() {
            Log.d("RemoteFactory", "onCreate");
        }

        @Override
        public void onDataSetChanged() {
            Log.d("RemoteFactory", "onDataSetChanged");
            refresh();
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
            return 20;
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
                courses = new ArrayList<>(1);
                courses.add(course);
            }

            Item addCourse(Course course) {
                if (start != course.begin_lesson) throw new IllegalArgumentException("Can't merge two course which have different start time");
                if (end < start + course.period) {
                    end = start + course.period;
                }
                // Don't use merged course text according to 产品规划运营部
                // text += "，" + course.toCourseString();
                if (courses == null) {
                    courses = new ArrayList<>(1);
                }
                courses.add(course);
                return this;
            }

            Item(int start) {
                this.start = start;
                this.end = start + 1;
                this.text = null;
                courses = null;
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

            public ArrayList<Course> getCourses() {
                return courses;
            }

            public boolean hasCourses() {
                return courses != null && courses.size() != 0;
            }

            @SuppressWarnings("StringBufferReplaceableByString")
            @Override
            public String toString() {
                return new StringBuilder().append("Item{").append("start=").append(start).append(", end=").append(end).append(", text='").append(text).append('\'').append('}').toString();
            }

            private int start;  // 三四节的 start 是 3
            private int end;  // 三四节课的 end 是 5
            private String text;
            private ArrayList<Course> courses;
        }

    }

}
