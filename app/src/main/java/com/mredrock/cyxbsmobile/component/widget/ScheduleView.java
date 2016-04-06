package com.mredrock.cyxbsmobile.component.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.Course;
import com.mredrock.cyxbsmobile.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScheduleView extends FrameLayout {

    private final int width = (int) ((DensityUtil.getScreenWidth(getContext()) - DensityUtil.dp2px(getContext(), 56)) / 7);
    private int height = (int) DensityUtil.dp2px(getContext(), 100);
    private CourseColorSelector colorSelector = new CourseColorSelector();
    public CourseList[][] course = new CourseList[7][7];
    private Context context;

    public ScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        /* 如果超大屏幕课表太短了，给它填满屏幕 */
        int screeHeight = DensityUtil.getScreenHeight(context);
        if (DensityUtil.px2dp(context, screeHeight) > 700) height = screeHeight / 6;
        initCourse();
        setWillNotDraw(false);
    }

    public ScheduleView(Context context) {
        super(context);
    }

    private void initCourse() {
        for (int i = 0; i < 7; i++) {
            course[i] = new CourseList[7];
        }
    }

    public void addContentView(List<Course> data) {
        removeAllViews();
        initCourse();
        if (data != null) {
            for (Course aData : data) {
                //colorSelector.addCourse(aData.course);
                colorSelector.addCourse(aData.begin_lesson, aData.hash_day);
                int x = aData.hash_lesson;
                int y = aData.hash_day;
                if (course[x][y] == null) {
                    course[x][y] = new CourseList();
                }
                course[x][y].list.add(aData);
            }
        }
        loadingContent();
    }

    private void loadingContent() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                if (course[x][y] != null) {
                    createLessonText(course[x][y]);
                }
            }
        }
        addAnchorView();
    }

    private void addAnchorView() {
        View anchor = new View(getContext());
        LayoutParams flparams = new LayoutParams(1, 1);
        flparams.topMargin = 0;
        flparams.leftMargin = width * 7 - 1;
        anchor.setBackgroundColor(Color.WHITE);
        anchor.setLayoutParams(flparams);
        addView(anchor);
    }


    private void createLessonText(final CourseList courses) {
        final Course course = courses.list.get(0);

        TextView tv = new TextView(context);
        int mTop = height * course.hash_lesson;
        int mLeft = width * course.hash_day;
        int mWidth = width;
        int mHeight = (int) (height * (float) course.period / 2);

        LayoutParams flParams = new LayoutParams((mWidth - DensityUtil.dp2px(getContext(), 1f)), (mHeight - DensityUtil.dp2px(getContext(), 1f)));
        flParams.topMargin = (mTop + DensityUtil.dp2px(getContext(), 1f));
        flParams.leftMargin = (mLeft + DensityUtil.dp2px(getContext(), 1f));
        tv.setLayoutParams(flParams);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setGravity(Gravity.CENTER);
        tv.setText(course.course + "@" + course.classroom);

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(DensityUtil.dp2px(getContext(), 1));
        //gd.setColor(colorSelector.getCourseColor(course.course));
        gd.setColor(colorSelector.getCourseColor(course.begin_lesson, course.hash_day));
        tv.setBackgroundDrawable(gd);
        tv.setOnClickListener(v -> CourseDialog.show(getContext(), courses));
        addView(tv);
        if (courses.list.size() > 1) {
            View drop = new View(getContext());
            drop.setBackgroundResource(R.drawable.ic_corner_right_bottom);
            LayoutParams dropLayout = new LayoutParams(mWidth / 5, mWidth / 5);
            dropLayout.topMargin = mTop + mHeight - mWidth / 5;
            dropLayout.leftMargin = mLeft + mWidth * 4 / 5;
            drop.setLayoutParams(dropLayout);
            addView(drop);
        }
    }

    public static class CourseList {
        public ArrayList<Course> list = new ArrayList<>();
    }

    public static class CourseColorSelector {
        private int[] colors = new int[]{
                Color.argb(255, 254, 145, 103),
                Color.argb(255, 120, 201, 252),
                Color.argb(255, 111, 219, 188),
                Color.argb(255, 191, 161, 233),
        };

        private HashMap<String, Integer> mCourseColorMap = new HashMap<>();

        @SuppressWarnings("unchecked")
        public void addCourse(String name) {
            Set set = mCourseColorMap.entrySet();
            for (Object aSet : set) {
                Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) aSet;
                if (entry.getKey().equals(name)) {
                    return;
                }
            }
            mCourseColorMap.put(name, colors[mCourseColorMap.size() % colors.length]);
        }

        public void addCourse(int beginLesson, int hashDay) {
            if (hashDay >= 5) mCourseColorMap.put(beginLesson + "," + hashDay, colors[3]);
            else if (beginLesson < 4) mCourseColorMap.put(beginLesson + "," + hashDay, colors[0]);
            else if (beginLesson < 7) mCourseColorMap.put(beginLesson + "," + hashDay, colors[1]);
            else mCourseColorMap.put(beginLesson + "," + hashDay, colors[2]);
        }

        public int getCourseColor(int beginLesson, int hashDay) {
            return mCourseColorMap.get(beginLesson + "," + hashDay);
        }

        public int getCourseColor(String name) {
            return mCourseColorMap.get(name);
        }
    }
}
