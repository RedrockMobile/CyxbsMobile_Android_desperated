package com.mredrock.cyxbsmobile.component.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.util.DensityUtils;

import java.util.ArrayList;

public class CourseDialog {

    public static void show(Context context, ScheduleView.CourseList list) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_schedule, null);
        RedPagerView view = (RedPagerView) layout.findViewById(R.id.course_viewpager);
        CoursePagerAdapter adapter = new CoursePagerAdapter(context, inflater, list);
        view.setAdapter(adapter);
        new AlertDialog.Builder(context)
                .setTitle("课程详细信息")
                .setView(layout, DensityUtils.dp2px(context, 12), DensityUtils.dp2px(context, 24), DensityUtils.dp2px(context, 12), DensityUtils.dp2px(context, 24))
                .show();
    }

    static class CoursePagerAdapter extends PagerAdapter {
        private ArrayList<Course> course;
        private LayoutInflater mInflater;
        private Context context;

        public CoursePagerAdapter(Context context, LayoutInflater mInflater, ScheduleView.CourseList course) {
            this.context = context;
            this.mInflater = mInflater;
            this.course = course.list;
        }

        @Override
        public int getCount() {
            return course.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View courseView = container.getChildAt(position);
            if (courseView == null) {
                Course course = this.course.get(position);
                courseView = mInflater.inflate(R.layout.item_dialog_schedule, container, false);
                TextView name = (TextView) courseView.findViewById(R.id.dialog_course_name);
                TextView teacher = (TextView) courseView.findViewById(R.id.dialog_course_teacher);
                TextView classroom = (TextView) courseView.findViewById(R.id.dialog_course_classroom);
                TextView weeks = (TextView) courseView.findViewById(R.id.dialog_course_weeks);
                TextView time = (TextView) courseView.findViewById(R.id.dialog_course_time);
                TextView timenum = (TextView) courseView.findViewById(R.id.dialog_course_time_num);
                TextView type = (TextView) courseView.findViewById(R.id.dialog_course_type);
                name.setText(course.course);
                teacher.setText(course.teacher);
                classroom.setText(course.classroom);
                time.setText(context.getResources().getStringArray(R.array.schedule_weekday)[course.hash_day]
                        + " ~ " + course.begin_lesson + "-" + (course.begin_lesson + course.period - 1) + "节");
                String timeStr = context.getResources().getStringArray(R.array.schedule_time)[course.hash_lesson];
                if (course.period == 2) {
                    timenum.setText(timeStr);
                } else {
                    timenum.setText(timeStr.substring(0, timeStr.indexOf("~") + 1) + "下课");
                }
                type.setText(course.type);
                weeks.setText(course.rawWeek);

                container.addView(courseView);
            }
            return courseView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
