package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.config.Config;
import com.mredrock.cyxbs.model.Affair;
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
            if (courses == null) {
                setError("你还没有登录");
                return;
            }
            if (courses.size() == 0) {
                // just show an empty list if there is no course.
                //setError("今天没有课");
            }
            generateItem(courses);
            setNormal();
        }

        @SuppressWarnings("deprecation")
        private int getItemColor(Item item) {
            if (item.start <= 4) {
                return getResources().getColor(R.color.color_course_morning);
            } else if (item.start <= 8) {
                return getResources().getColor(R.color.color_course_afternoon);
            } else {
                return getResources().getColor(R.color.color_course_night);
            }
        }

        private @DrawableRes int getCornerDrawableId(Item item) {
            if ((item.getType() & Item.ITEM_TYPE_COURSE_ONLY) != 0) {
                return R.drawable.ic_regular_triangle_white;
            } else if (item.start <= 4) {
                return R.drawable.ic_regular_triangle_blue;
            } else if (item.start <= 8) {
                return R.drawable.ic_regular_triangle_orange;
            } else {
                return R.drawable.ic_regular_triangle_teal;
            }
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
                Item item = items.valueAt(i);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_course_list_item);
                // initialize the view first to prevent views repetition
                views.setInt(R.id.rl_app_widget_course_item_content, "setBackgroundColor", 0xFFFFFFFF);
                views.setTextViewText(R.id.tv_app_widget_course_item_name, "");
                views.setTextViewText(R.id.tv_app_widget_course_item_room, "");
                views.setInt(R.id.iv_app_widget_course_item_corner, "setVisibility", View.INVISIBLE);
                views.setBoolean(R.id.rl_app_widget_course_item_content, "setEnabled", false);
                // check course type
                if ((item.getType() & Item.ITEM_TYPE_COURSE_ONLY) == 0) {  // empty or affair only
                    Log.d("getViewAt", "Position: " + i + ", Empty: " + item.getOrderString() + ", Type: " + item.getType());
                    views.setTextViewText(R.id.tv_app_widget_course_item_order, item.getOrderString());
                    if ((item.getType() & Item.ITEM_TYPE_AFFAIR_ONLY) != 0) { // affair only
                        Intent coursesIntent = new Intent();
                        coursesIntent.putExtra(CourseListAppWidget.EXTRA_COURSES, item.getCourses().toArray());
                        views.setOnClickFillInIntent(R.id.rl_app_widget_course_item_content, coursesIntent);
                        views.setBoolean(R.id.rl_app_widget_course_item_content, "setEnabled", true);
                        views.setImageViewResource(R.id.iv_app_widget_course_item_corner, getCornerDrawableId(item));  // colorful
                        views.setInt(R.id.iv_app_widget_course_item_corner, "setVisibility", View.VISIBLE);
                    }
                } else {
                    Log.d("getViewAt", "Position: " + i + ", Normal: " + item.getOrderString() + ", \n" + item.getText() + ", Type: " + item.getType());
                    views.setTextViewText(R.id.tv_app_widget_course_item_order, item.getOrderString());
                    views.setTextViewText(R.id.tv_app_widget_course_item_name, item.getText());
                    views.setTextViewText(R.id.tv_app_widget_course_item_room, item.getClassroom());
                    views.setInt(R.id.rl_app_widget_course_item_content, "setBackgroundColor", getItemColor(item));
                    Intent coursesIntent = new Intent();
                    coursesIntent.putParcelableArrayListExtra(CourseListAppWidget.EXTRA_COURSES, item.getCourses());
                    views.setOnClickFillInIntent(R.id.rl_app_widget_course_item_content, coursesIntent);
                    views.setBoolean(R.id.rl_app_widget_course_item_content, "setEnabled", true);
                    if ((item.getType() & Item.ITEM_TYPE_AFFAIR_ONLY) != 0) { // affair & course
                        views.setImageViewResource(R.id.iv_app_widget_course_item_corner, R.drawable.ic_regular_triangle_white);
                        views.setInt(R.id.iv_app_widget_course_item_corner, "setVisibility", View.VISIBLE);
                    }
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
                    if (items.indexOfKey(c.getBeginLesson()) < 0) {  // fill course
                        items.put(c.getBeginLesson(), new Item(c));
                    } else {  // merge course
                        items.put(c.getBeginLesson(), items.get(c.getBeginLesson()).addCourse(c));
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

            public static final int ITEM_TYPE_EMPTY = 0b00;
            public static final int ITEM_TYPE_COURSE_ONLY = 0b01;
            public static final int ITEM_TYPE_AFFAIR_ONLY = 0b10;
            public static final int ITEM_TYPE_COURSE_AFFAIR = 0b11;

            Item(Course course) {
                start = course.getBeginLesson();
                courses = new ArrayList<>(1);
                if (course.getCourseType() == Affair.TYPE) {  // is affair
                    type |= ITEM_TYPE_AFFAIR_ONLY;
                    end = start + 1;    // don't use affair's end time
                    text = "";          // don't use affair's text
                } else {  // is course
                    type |= ITEM_TYPE_COURSE_ONLY;
                    text = course.course;
                    classroom = course.classroom;
                    end = course.getBeginLesson() + course.period;
                }
                courses.add(course);
            }

            Item addCourse(Course course) {
                if (start != course.getBeginLesson()) throw new IllegalArgumentException("Can't merge two course which have different start time");
                // Don't use merged course text according to 产品规划运营部
                // text += "，" + course.toCourseString();
                if (courses == null) {
                    courses = new ArrayList<>(1);
                }
                if (course.getCourseType() == Affair.TYPE) {    // is affair
                    type |= ITEM_TYPE_AFFAIR_ONLY;
                    if ((type & ITEM_TYPE_COURSE_ONLY) == 0) {  // no course here
                        end = start + 1;    // don't use affair's end time
                    }
                } else {    // is course
                    if ((type & ITEM_TYPE_COURSE_ONLY) == 0) {  // no course yet
                        text = course.course;
                        classroom = course.classroom;
                    }
                    type |= ITEM_TYPE_COURSE_ONLY;
                    if (end < start + course.period) {
                        end = start + course.period;
                    }
                }
                courses.add(course);
                return this;
            }

            Item(int start) {
                this.start = start;
                this.end = start + 1;
                this.text = null;
                this.classroom = null;
                courses = null;
            }

            String getText() {
                Log.v("Item", "getText: " + text);
                return text;
            }

            public String getClassroom() {
                return classroom;
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

            public int getType() {
                return type;
            }

            @SuppressWarnings("StringBufferReplaceableByString")
            @Override
            public String toString() {
                return new StringBuilder().append("Item{").append("start=").append(start).append(", end=").append(end).append(", text='").append(text).append('\'').append('}').toString();
            }

            private int start;  // 三四节的 start 是 3
            private int end;  // 三四节课的 end 是 5
            private String text;
            private String classroom;
            private ArrayList<Course> courses;
            private int type = ITEM_TYPE_EMPTY;
        }

    }

}
