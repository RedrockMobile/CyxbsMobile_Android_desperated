package com.mredrock.cyxbs.ui.adapter.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Exam;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamScheduleAdapter extends RecyclerView.Adapter<ExamScheduleAdapter.ExamViewHolder> {
    private static final int[] IDS = new int[] {R.drawable.circle_pink, R.drawable.circle_blue, R.drawable.circle_yellow};

    private Context mContext;
    private List<Exam> mExamList;
    private ExamDataHelper mExamDataHelper;

    public ExamScheduleAdapter(Context context, List<Exam> examList) {
        mContext = context;
        mExamList = examList;
        mExamDataHelper = new ExamDataHelper();
    }

    @Override
    public ExamScheduleAdapter.ExamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_exam, parent, false);
            return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamScheduleAdapter.ExamViewHolder viewHolder, int position) {
        bindCommonViewHolder(viewHolder, position);
    }

    @SuppressLint("DefaultLocale")
    private void bindCommonViewHolder(ExamViewHolder holder, int position) {
        Exam exam = mExamList.get(position);
        if (exam.week != null && exam.weekday != null) {
            SchoolCalendar schoolCalendar = null;
            if (Integer.valueOf(exam.week) != 0) {
                schoolCalendar = new SchoolCalendar(Integer.valueOf(exam.week),
                        Integer.valueOf(exam.weekday));
            }
            boolean isSuccess = mExamDataHelper.tryModifyData(exam);
            if (isSuccess && schoolCalendar != null) {
                holder.dayOfWeek.setText(String.format("%s周周%s", exam.week, exam.chineseWeekday));
                holder.dayOfMonth.setText(String.format("%d", schoolCalendar.getDay()));
                holder.month.setText(String.format("%d月", schoolCalendar.getMonth()));
            } else {
                holder.dayOfWeek.setText(String.format("%s周周%s", "-", "-"));
                holder.dayOfMonth.setText(String.format("%s", "-"));
                holder.month.setText(String.format("%s月", ""));
            }
        } else {
            // TODO: 2017/7/23 不知道的玩意儿，暂时删除
            //holder.mTvExamTime.setText(exam.date);
        }
        holder.examName.setText(exam.course);
        String seat = Integer.valueOf(exam.seat) != 0 ? exam.seat : "--";
        if (seat.length() < 2) {
            seat = 0 + seat;
        }
        seat += "号";
        holder.location.setText(exam.classroom + "场" + seat);
        if (exam.begin_time != null && exam.end_time != null) {
            holder.time.setText(String.format(mContext.getResources()
                    .getString(R.string.exam_detail_date), exam.begin_time, exam.end_time));
        } else {
            holder.time.setText(exam.time);
        }

        holder.circle.setImageResource(IDS[position % 3]);
        if (position == getItemCount() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mExamList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day_of_month)
        TextView dayOfMonth;
        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.circle)
        ImageView circle;
        @BindView(R.id.exam_name)
        TextView examName;
        @BindView(R.id.day_of_week)
        TextView dayOfWeek;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.line)
        View line;

        public ExamViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private class ExamDataHelper {
        private final String[] numArray = {"1", "2", "3", "4", "5", "6", "7"};
        private final String[] numChineseArray = {"一", "二", "三", "四", "五", "六", "七"};

        public boolean tryModifyData(Exam exam) {
            if (exam.weekday.equals("0") || exam.week.equals("0")) {
                return false;
            } else {
                toChineseNum(exam);
            }
            return true;
        }

        private void toChineseNum(Exam exam) {
            for (int i = 0; i < numArray.length; i++) {
                if (exam.weekday.equals(numArray[i])) {
                    // 这里不应该改变原有的值
                    //exam.weekday = numChineseArray[i];
                    exam.chineseWeekday = numChineseArray[i];
                }
            }
        }
    }

}
