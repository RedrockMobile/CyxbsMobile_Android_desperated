package com.mredrock.cyxbs.component.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.AffairDeleteEvent;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.util.DensityUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CourseDialog {

    public static void show(Context context, ScheduleView.CourseList list) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_schedule, null);
        RedPagerView view = (RedPagerView) layout.findViewById(R.id.course_viewpager);

        if (list.list.size() == 1 && list.list.get(0).getCourseType() == 2) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = DensityUtils.dp2px(context, 220);
            view.setLayoutParams(params);
        }

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("详细信息")
                .setCancelable(true)
                .setView(layout, DensityUtils.dp2px(context, 12), DensityUtils.dp2px(context, 24), DensityUtils.dp2px(context, 12), DensityUtils.dp2px(context, 24))
                .create();

        CoursePagerAdapter adapter = new CoursePagerAdapter(context, inflater, list, dialog);
        view.setAdapter(adapter);
        dialog.show();


    }

    static class CoursePagerAdapter extends PagerAdapter {
        private ArrayList<Course> course;
        private LayoutInflater mInflater;
        private Context context;
        private Dialog dialog;

        public CoursePagerAdapter(Context context, LayoutInflater mInflater,
                                  ScheduleView.CourseList course, Dialog dialog) {
            this.context = context;
            this.mInflater = mInflater;
            this.course = course.list;
            this.dialog = dialog;
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
            Course course = this.course.get(position);
            if (courseView == null) {
                if (course.getCourseType() == Course.TYPE) {
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
                } else {
                    courseView = mInflater.inflate(R.layout.item_dialog_affair, container, false);
                    TextView name = (TextView) courseView.findViewById(R.id.dialog_course_name);
                    TextView teacher = (TextView) courseView.findViewById(R.id.dialog_course_teacher);
                    Button delete = (Button) courseView.findViewById(R.id.dialog_affair_delete);
                    Button modify = (Button) courseView.findViewById(R.id.dialog_affair_modify);
                    delete.setOnClickListener((view -> {
                        EventBus.getDefault().post(new AffairDeleteEvent(course));
                        dialog.dismiss();
                    }));
                    modify.setOnClickListener((view -> {

                    }));
                    name.setText(course.course);
                    teacher.setText(course.teacher);
                    container.addView(courseView);

                }

            }
            return courseView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
