package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.util.DensityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScheduleView extends FrameLayout {

    private final int width = (int) ((DensityUtils.getScreenWidth(getContext()) - DensityUtils.dp2px(getContext(), 56)) / 7);
    private int height = (int) DensityUtils.dp2px(getContext(), 100);
    private CourseColorSelector colorSelector = new CourseColorSelector();
    public CourseList[][] course = new CourseList[7][7];

    private Context context;
    private ImageView mClickImageView;
    private int startX, startY, endX, endY;
    private boolean showMode = true; //是否在课表界面显示事项内容





    private OnImageViewClickListener onImageViewClickListener;


    public interface OnImageViewClickListener {
        void onClick(int x, int y);
    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        /* 如果超大屏幕课表太短了，给它填满屏幕 */
        int screeHeight = DensityUtils.getScreenHeight(context);
        if (DensityUtils.px2dp(context, screeHeight) > 700) height = screeHeight / 6;
        initCourse();
        setWillNotDraw(false);
    }

    public ScheduleView(Context context) {
        super(context);
    }

    private void initCourse() {
        for (int i = 0; i < 7; i++) {
            if (course[i] == null)
                course[i] = new CourseList[7];
        }
    }

    public void addContentView(List<Course> data) {
        removeAllViews();
        initCourse();
        if (data != null) {
            for (Course aData : data) {
                if (aData.getCourseType() == 1)
                    colorSelector.addCourse(aData.begin_lesson,aData.hash_day);
                else
                    colorSelector.addAffair(aData.begin_lesson,aData.hash_day);
                //colorSelector.addCourse(aData.begin_lesson, aData.hash_day);
                int x = aData.hash_day;
                int y = aData.hash_lesson;
                if (course[x][y] == null) {
                    course[x][y] = new CourseList();
                }
                course[x][y].list.add(aData);
                Collections.sort(course[x][y].list, ((course1, t1) ->
                        course1.getCourseType() - t1.getCourseType()
                ));
            }
        }
        loadingContent();
    }

    public void setOnImageViewClickListener(OnImageViewClickListener onImageViewClickListener) {
        this.onImageViewClickListener = onImageViewClickListener;
    }

    private void loadingContent() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                if (course[x][y] != null && course[x][y].list != null && course[x][y].list.size() != 0) {
                    createLessonText(course[x][y]);
                }
            }
        }
        addAnchorView();
    }

    public void clearList() {
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[0].length; j++) {
                if (course[i][j] != null && course[i][j].list != null)
                    course[i][j].list.clear();
            }
        }

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

        LayoutParams flParams = new LayoutParams((mWidth - DensityUtils.dp2px(getContext(), 1f)), (mHeight - DensityUtils.dp2px(getContext(), 1f)));
        flParams.topMargin = (mTop + DensityUtils.dp2px(getContext(), 1f));
        flParams.leftMargin = (mLeft + DensityUtils.dp2px(getContext(), 1f));
        tv.setLayoutParams(flParams);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setGravity(Gravity.CENTER);
        tv.setText(course.getCourseType() == 1 ? course.toCourseString() : " ");

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(DensityUtils.dp2px(getContext(), 1));
        if (course.getCourseType() == 1)
            gd.setColor(colorSelector.getCourseColor(course.begin_lesson,course.hash_day));
        else{
            if (showMode){
                gd.setColor(colorSelector.getAffairColor());
                tv.setText(course.course);
            }else
                gd.setColor(colorSelector.getAffairColor(course.begin_lesson,course.hash_day));
        }
           // gd.setColor(showMode ? colorSelector.getCourseColor() : colorSelector.getAffairColor(course.begin_lesson,course.hash_day));
        tv.setBackgroundDrawable(gd);
        tv.setOnClickListener(v -> {
            CourseList courseList = new CourseList();
            courseList.list.addAll(courses.list);
            CourseDialog.show(getContext(), courseList);
        });
        addView(tv);
        if (!showMode){
            if (courses.list.get(0).getCourseType() == Affair.TYPE){
                View drop = new View(getContext());
                int beginLesson = courses.list.get(0).hash_lesson;

                if (beginLesson < 2){
                    drop.setBackgroundResource(R.drawable.ic_corner_right_top_blue);
                }
                else if (beginLesson < 4){
                    drop.setBackgroundResource(R.drawable.ic_corner_right_top_yellow);
                }
                else{
                    drop.setBackgroundResource(R.drawable.ic_corner_right_top_green);
                }

                LayoutParams dropLayout = new LayoutParams(mWidth / 5, mWidth / 5);
                dropLayout.topMargin = mTop + DensityUtils.dp2px(context, 3);
                dropLayout.leftMargin = mLeft + mWidth * 4 / 5 - DensityUtils.dp2px(context,1);
                drop.setLayoutParams(dropLayout);
                addView(drop);
            }
        }


        if (courses.list.size() > 1) {
            if (courses.list.get(courses.list.size() - 1).getCourseType() == 2) {
                View drop = new View(getContext());
                drop.setBackgroundResource(R.drawable.ic_corner_right_top);
                LayoutParams dropLayout = new LayoutParams(mWidth / 5, mWidth / 5);
                dropLayout.topMargin = mTop + DensityUtils.dp2px(context, 3);
                dropLayout.leftMargin = mLeft + mWidth * 4 / 5 - DensityUtils.dp2px(context, 1);
                drop.setLayoutParams(dropLayout);
                addView(drop);
            }
            if (courses.list.get(1).getCourseType() == 1) {
                View drop = new View(getContext());
                drop.setBackgroundResource(R.drawable.ic_corner_right_bottom);
                LayoutParams dropLayout = new LayoutParams(mWidth / 5, mWidth / 5);
                dropLayout.topMargin = mTop + mHeight - mWidth / 5;
                dropLayout.leftMargin = mLeft + mWidth * 4 / 5;
                drop.setLayoutParams(dropLayout);
                addView(drop);
            }


        }
    }

    public static class CourseList {
        public ArrayList<Course> list = new ArrayList<>();
    }

    public void addDropTopView(){

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
/*        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = (int) event.getX();
            startY = (int) event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            endX = (int) event.getX();
            endY = (int) event.getY();
        }
        int distance = (int) Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
        if (distance <= 2) {
            int x = (int) (event.getX() / getWidth() * 7);
            int y = (int) (event.getY() / getHeight() * 12);
            if (course[x][y / 2] == null || course[x][y / 2].list == null || course[x][y / 2].list.size() == 0){
                if (mClickImageView == null) {
                    mClickImageView = new ImageView(context);
                    mClickImageView.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.ic_add_circle));
                    mClickImageView.setBackgroundColor(Color.parseColor("#60000000"));
                    mClickImageView.setScaleType(ImageView.ScaleType.CENTER);

                }
                if (onImageViewClickListener != null) {
                    mClickImageView.setOnClickListener((view -> onImageViewClickListener.onClick(x, y)));
                }
                int mTop = height * y / 2;
                int mLeft = width * x;
                int mWidth = width;
                int mHeight = (int) (height * (float) 1 / 2);
                LayoutParams flParams = new LayoutParams((mWidth - DensityUtils.dp2px(getContext(), 1f)), (mHeight - DensityUtils.dp2px(getContext(), 1f)));
                flParams.topMargin = (mTop + DensityUtils.dp2px(getContext(), 1f));
                flParams.leftMargin = (mLeft + DensityUtils.dp2px(getContext(), 1f));
                mClickImageView.setLayoutParams(flParams);
                removeView(mClickImageView);
                addView(mClickImageView);
            }


        }

        return true;*/
        return false;

    }

    public boolean isShowMode() {
        return showMode;
    }

    public void setShowMode(boolean showMode) {
        this.showMode = showMode;
    }

    public static class CourseColorSelector {
        private int[] colors = new int[]{
//                Color.argb(200, 254, 145, 103),
//                Color.argb(200, 120, 201, 252),
//                Color.argb(200, 111, 219, 188),
//                Color.argb(200, 191, 161, 233),

                Color.parseColor("#64d2f7"),
                Color.parseColor("#f9af58"),
                Color.parseColor("#79dbc4"),
                Color.parseColor("#bdc3c7")
        };

        private int[] affairColors = new int[]{
//                Color.argb(200, 254, 145, 103),
//                Color.argb(200, 120, 201, 252),
//                Color.argb(200, 111, 219, 188),
//                Color.argb(200, 191, 161, 233),

                Color.parseColor("#e8f5fe"),
                Color.parseColor("#fff5e9"),
                Color.parseColor("#e6fffb"),
//                Color.parseColor("#bdc3c7")
        };

        private HashMap<String, Integer> mCourseColorMap = new HashMap<>();
        private HashMap<String, Integer> mAffairColorMap = new HashMap<>();

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
            else if (beginLesson < 9) mCourseColorMap.put(beginLesson + "," + hashDay, colors[1]);
            else mCourseColorMap.put(beginLesson + "," + hashDay, colors[2]);
        }

        public void addAffair(int beginLesson, int hashDay) {
//            if (hashDay >= 5) mAffairColorMap.put(beginLesson + "," + hashDay, colors[3]);
//            else
            if (beginLesson < 4)
                mAffairColorMap.put(beginLesson + "," + hashDay, affairColors[0]);
            else if (beginLesson < 9)
                mAffairColorMap.put(beginLesson + "," + hashDay, affairColors[1]);
            else
                mAffairColorMap.put(beginLesson + "," + hashDay, affairColors[2]);
        }

        public int getCourseColor(int beginLesson, int hashDay) {
            return mCourseColorMap.get(beginLesson + "," + hashDay);
        }

        public int getAffairColor(int beginLesson, int hashDay) {
            return mAffairColorMap.get(beginLesson + "," + hashDay);
        }

        public int getCourseColor(String name) {
            return mCourseColorMap.get(name);
        }

        public int getAffairColor(){
            return colors[3];
        }
    }


}
