package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.NoCourse;
import com.mredrock.cyxbs.ui.adapter.me.NoCourseDialogAdapter;
import com.mredrock.cyxbs.util.DensityUtils;

import java.util.List;

/**
 * Created by skylineTan on 2016/4/13 23:30.
 */
public class NoScheduleView extends FrameLayout {

    private final int width = (int) ((DensityUtils.getScreenWidth(getContext()) - DensityUtils.dp2px(getContext(), 26)) / 7);
    private final int height = (int) (DensityUtils.dp2px(getContext(), 100));
    private NoCourseColorSelector colorSelector = new NoCourseColorSelector();
    public NoCourse[][] noCourseArray = new NoCourse[7][7];
    private Context context;


    public NoScheduleView(Context context) {
        super(context);
    }

    public NoScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        setWillNotDraw(false);
    }

    public void addContentView(List<NoCourse> noCourseList, int week) {
        removeAllViews();
        init();
        if (noCourseList != null) {
            for (NoCourse noCourse : noCourseList) {
                int x = noCourse.hash_day;
                int y = noCourse.hash_lesson;
                noCourseArray[x][y] = noCourse;
            }
        }
        loadingContent(week);
    }


    private void loadingContent(int week) {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                if (noCourseArray[x][y] != null) {
                    createTextView(noCourseArray[x][y], week);
                }
            }
        }
    }


    private void createTextView(NoCourse noCourse, int week) {
        TextView textView = new TextView(getContext());
        int top = height * noCourse.hash_lesson;
        int left = width * noCourse.hash_day;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (width - DensityUtils.dp2px(getContext(), 1f)), (height - DensityUtils.dp2px(getContext(), 1f)));
        params.topMargin = top + DensityUtils.dp2px(getContext(), 1f);
        params.leftMargin = left + DensityUtils.dp2px(getContext(), 1f);
        textView.setLayoutParams(params);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setGravity(Gravity.CENTER);
        StringBuilder sb = new StringBuilder();
        if (week == 0) {
            if (noCourse.names.size() <= 2) {
                for (String name : noCourse.names) {
                    sb.append(name).append("\n");
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    sb.append(noCourse.names.get(i)).append("\n");
                }
            }
        } else {
            if (noCourse.names.size() <= 5) {
                for (String s : noCourse.names)
                    sb.append(s).append('\n');
            } else {
                for (int i = 0; i < 5; i++) {
                    sb.append(noCourse.names.get(i)).append("\n");
                }
            }
        }
        textView.setText(sb+"有空");
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(DensityUtils.dp2px(getContext(), 1));
        gd.setColor(colorSelector.getNoCourseColor(noCourse.hash_day,
                noCourse.hash_lesson));
        textView.setBackgroundDrawable(gd);
        textView.setOnClickListener(v -> showDetailDialog(noCourse));
        addView(textView);
        if (week == 0 && noCourse.names.size() > 2) {
            addDropTriangle(top, left);
        } else if (noCourse.names.size() > 5) {
            addDropTriangle(top, left);
        }
    }

    private void init() {
        for (int i = 0; i < 7; i++) {
            noCourseArray[i] = new NoCourse[7];
        }
    }

    private void addDropTriangle(int top, int left) {
        View drop = new View(getContext());
        drop.setBackgroundDrawable(getResources().getDrawable(R.drawable
                .ic_corner_right_bottom));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams
                (width / 5, height / 5);
        layoutParams.topMargin = top + 4 * height / 5;
        layoutParams.leftMargin = left + 4 * height / 5;
        drop.setLayoutParams(layoutParams);
        addView(drop);
    }

    private void showDetailDialog(NoCourse noCourse) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_no_schedule, null);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.no_course_recycler);
        TextView day = (TextView) layout.findViewById(R.id.dialog_no_course_day);
        TextView lesson = (TextView) layout.findViewById(R.id.dialog_no_course_lesson);
        TextView time = (TextView) layout.findViewById(R.id.dialog_no_course_time);
        TextView count = (TextView) layout.findViewById(R.id.no_course_count);
        TextView certain = (TextView) layout.findViewById(R.id.dialog_no_course_certain);
        day.setText(context.getResources().getStringArray(R.array
                .schedule_weekday)[noCourse.hash_day]);
        lesson.setText(context.getResources().getStringArray(R.array
                .no_schedule_sections)[noCourse.hash_lesson]);
        time.setText(context.getResources().getStringArray(R.array
                .schedule_time)[noCourse.hash_lesson]);
        count.setText(new StringBuilder("共计").append(noCourse.names.size()).append("人"));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        NoCourseDialogAdapter adapter = new NoCourseDialogAdapter(noCourse.names, context);
        recyclerView.setAdapter(adapter);
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("详情")
                .customView(layout, true)
                .show();
        certain.setOnClickListener(view -> dialog.dismiss());
    }

    public static class NoCourseColorSelector {
        private int[] colors = new int[]{
                Color.argb(200, 254, 145, 103),
                Color.argb(200, 120, 201, 252),
                Color.argb(200, 111, 219, 188),
                Color.argb(200, 191, 161, 233),
        };

        public int getNoCourseColor(int hashDay, int hashLesson) {
            if (hashDay == 5 || hashDay == 6) {
                return colors[3];
            }
            switch (hashLesson) {
                case 0:
                case 1:
                    return colors[0];
                case 2:
                case 3:
                    return colors[1];
                case 4:
                case 5:
                    return colors[2];
            }
            return colors[2];
        }
    }
}
