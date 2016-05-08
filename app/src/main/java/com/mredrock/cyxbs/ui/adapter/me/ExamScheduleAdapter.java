package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Exam;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.List;

public class ExamScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int NORMAL = 1;

    private Context mContext;
    private List<Exam> mExamList;
    private ExamDataHelper mExamDataHelper;

    public ExamScheduleAdapter(Context context, List<Exam> examList) {
        mContext = context;
        mExamList = examList;
        mExamDataHelper = new ExamDataHelper();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return NORMAL;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_exam_first, parent, false);
            return new ExamHeaderViewHolder(view);
        } else {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_exam, parent, false);
            return new ExamCommonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = viewHolder.getItemViewType();
        if (type == HEADER) {
            bindHeadViewHolder((ExamHeaderViewHolder) viewHolder);
        } else {
            bindCommonViewHolder((ExamCommonViewHolder) viewHolder, position);
        }
    }

    private void bindHeadViewHolder(ExamHeaderViewHolder holder) {
        Exam exam = mExamList.get(0);
        if (exam.week != null && exam.weekday != null) {
            SchoolCalendar schoolCalendar = null;
            if (exam.week != null && Integer.valueOf(exam.week) != 0) {
                schoolCalendar = new SchoolCalendar(Integer.valueOf(exam.week),
                        Integer.valueOf(exam.weekday));
            }
            boolean isSuccess = mExamDataHelper.tryModifyData(exam);
            if (isSuccess && schoolCalendar != null) {
                //Log.d("aaa", exam.week + "  " + exam.weekday);
                holder.mTvExamTime.setText(formatData(mContext.getResources().getString(R.string.exam_date),
                        exam.week, exam.chineseWeekday) + "\n" +
                        schoolCalendar.getMonth() + "/" +
                        schoolCalendar.getDay());
            } else {
                holder.mTvExamTime.setText(mContext.getResources().getString(R.string.exam_unsure));
            }
        } else {
            holder.mTvExamTime.setText(exam.date);
        }
        holder.mTvExamCourse.setText(exam.course);
        String seat = Integer.valueOf(exam.seat) != 0 ? " @ " + exam.seat : "";
        holder.mTvExamLocation.setText(exam.classroom + seat);
        if (exam.begin_time != null && exam.end_time != null) {
            holder.mTvExamDaytime.setText(formatData(mContext.getResources()
                            .getString(R.string.exam_detail_date),
                    exam.begin_time, exam.end_time));
        } else {
            holder.mTvExamDaytime.setText(exam.time);
        }
    }

    private void bindCommonViewHolder(ExamCommonViewHolder holder, int position) {
        Exam exam = mExamList.get(position);
        if (exam.week != null && exam.weekday != null) {
            SchoolCalendar schoolCalendar = null;
            if (Integer.valueOf(exam.week) != 0) {
                schoolCalendar = new SchoolCalendar(Integer.valueOf(exam.week),
                        Integer.valueOf(exam.weekday));
            }
            boolean isSuccess = mExamDataHelper.tryModifyData(exam);
            if (isSuccess && schoolCalendar != null) {
                holder.mTvExamTime.setText(formatData(mContext.getResources().getString(R.string.exam_date),
                        exam.week, exam.chineseWeekday) + "\n" +
                        schoolCalendar.getMonth() + "/" +
                        schoolCalendar.getDay());
            } else {
                holder.mTvExamTime.setText(mContext.getResources().getString(R.string.exam_unsure));
            }
        } else {
            holder.mTvExamTime.setText(exam.date);
        }
        holder.mTvExamCourse.setText(exam.course);
        String seat = Integer.valueOf(exam.seat) != 0 ? " @ " + exam.seat : "";
        holder.mTvExamLocation.setText(exam.classroom + seat);
        if (exam.begin_time != null && exam.end_time != null) {
            holder.mTvExamDaytime.setText(formatData(mContext.getResources()
                    .getString(R.string.exam_detail_date), exam.begin_time, exam.end_time));
        } else {
            holder.mTvExamDaytime.setText(exam.time);
        }
    }

    private String formatData(String format, String... args) {
        if (args.length != 2)
            return "";
        return String.format(format, args[0], args[1]);
    }


    @Override
    public int getItemCount() {
        return mExamList.size();
    }

    public class ExamHeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_exam_first_tv_time)
        TextView mTvExamTime;
        @Bind(R.id.item_exam_first_tv_course)
        TextView mTvExamCourse;
        @Bind(R.id.item_exam_first_tv_location)
        TextView mTvExamLocation;
        @Bind(R.id.item_exam_first_day_time)
        TextView mTvExamDaytime;

        public ExamHeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ExamCommonViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_exam_tv_time)
        TextView mTvExamTime;
        @Bind(R.id.item_exam_tv_course)
        TextView mTvExamCourse;
        @Bind(R.id.item_exam_tv_location)
        TextView mTvExamLocation;
        @Bind(R.id.item_exam_tv_day_time)
        TextView mTvExamDaytime;

        public ExamCommonViewHolder(View view) {
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
