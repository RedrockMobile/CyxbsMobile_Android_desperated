package com.mredrock.cyxbs.ui.activity.affair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Position;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.event.TimeChooseEvent;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.ui.fragment.affair.TimeChooseFragment;
import com.mredrock.cyxbs.util.KeyboardUtils;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mredrock.cyxbs.util.LogUtils.LOGE;
import static u.aly.av.P;
import static u.aly.av.S;
import static u.aly.av.c;
import static u.aly.av.p;
import static u.aly.av.s;


public class EditAffairActivity extends AppCompatActivity {

    private static final String COURSE_KEY = "course";
    private final String[] TIMES = new String[]{"不提醒", "提前5分钟", "提前10分钟", "提前20分钟", "提前30分钟", "提前一个小时"};
    private final String[] WEEKS = {"周一","周二","周三","周四","周五","周六","周日"};
    private final String[] CLASSES = {"一二节","三四节","五六节","七八节","九十节","AB节"};
    BottomSheetBehavior behavior;

    @Bind(R.id.add_affair_title_edit)
    EditText mEditTitle;
    @Bind(R.id.add_affair_content_edit)
    EditText mEditContent;

    @Bind(R.id.edit_affair_remind_layout)
    RelativeLayout chooseRemindTimeLayout;

    @Bind(R.id.edit_affair_tv_remind_time)
    TextView mRemindTimeText;

    @Bind(R.id.edit_affair_tv_weeks)
    TextView mWeekText;

    @Bind(R.id.edit_affair_tv_choose_time)
    TextView mTimeChooseText;

    @Bind(R.id.edit_affair_rv_weeks_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.edit_affair_et_content)
    EditText mContentEdit;

    @Bind(R.id.edit_affair_et_title)
    EditText mTitleEdit;

    private List<Integer> weeks = new ArrayList<>();
    private WeekAdapter mWeekAdapter;
    private ArrayList<Position> positions = new ArrayList<>();


    @OnClick(R.id.edit_affair_remind_layout)
    public void onRemindTimeClick(View v){
        KeyboardUtils.hideInput(v);
        new AlertDialog.Builder(this).setTitle("选择提醒时间")
                .setItems(TIMES,(dialog,i)->{
                    LOGE("EditAffairActivity",i+"");
                    mRemindTimeText.setText(TIMES[i]);
                }).show();

    }

    @OnClick(R.id.edit_affair_weeks_layout)
    public void onWeekChooseClick(View v){
        KeyboardUtils.hideInput(v);
        intro();
    }

    @OnClick(R.id.edit_affair_time_layout)
    public void onTimeChooseClick(View v){
        KeyboardUtils.hideInput(v);
        Intent i = new Intent(this,TimeChooseActivity.class);
        i.putExtra(TimeChooseActivity.BUNDLE_KEY,positions);
        startActivity(i);
    }

    @OnClick(R.id.edit_affair_iv_week_ok)
    public void onWeekChooseOkClick(){
        weeks.clear();
        weeks.addAll(mWeekAdapter.getWeeks());
        Collections.sort(weeks);
        LogUtils.LOGE("EditAffairActivity",weeks.toString());
        String data = weeks.toString();
        data = data.substring(1,data.length()-1);
        mWeekText.setText("第"+data+"周");
        intro();
    }

    @OnClick(R.id.edit_affair_iv_save)
    public void onSaveClick(View v){
        KeyboardUtils.hideInput(v);
        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        if (!title.trim().isEmpty() && !content.trim().isEmpty()){
            Toast.makeText(APP.getContext(),"标题和内容不能为空哦",Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this,R.color.white_black);
        setContentView(R.layout.activity_edit_affair);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        behavior = BottomSheetBehavior.from(findViewById(R.id.scroll));
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        initView();
    }

    private void initView() {
        mEditContent.setOnFocusChangeListener((view, b) -> {
            if (b)
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
        mEditTitle.setOnFocusChangeListener((view, b) -> {
            if (b)
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mWeekAdapter = new WeekAdapter();
        mRecyclerView.setAdapter(mWeekAdapter);
        mRecyclerView.smoothScrollToPosition(15);
    }

    public static void editAffairActivityStart(Context context, Course course) {
        Intent starter = new Intent(context, EditAffairActivity.class);
        starter.putExtra(COURSE_KEY, (Parcelable) course);
        context.startActivity(starter);
    }

    public void intro() {
        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeChooseEvent(TimeChooseEvent event) {
        LogUtils.LOGE("EditAffairActivity",event.getPositions().toString());
        positions.clear();
        positions.addAll(event.getPositions());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < positions.size() && i < 3; i++) {
             stringBuffer.append(WEEKS[positions.get(i).getX()]+CLASSES[positions.get(i).getY()]+" ");
        }
        mTimeChooseText.setText(stringBuffer.toString());
    }




    class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {
        private List<String> weeks = new ArrayList<>();
        private Set<Integer> mWeeks = new HashSet<>();




        public WeekAdapter() {
            weeks.addAll(Arrays.asList(EditAffairActivity.this.getResources().getStringArray(R.array.titles_weeks)));
            weeks.remove(0);
        }

        @Override
        public WeekAdapter.WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeekAdapter.WeekViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_week, parent, false));
        }

        public Set<Integer> getWeeks() {
            return mWeeks;
        }

        public void setmWeeks(Set<Integer> mWeeks) {
            this.mWeeks = mWeeks;
        }

        @Override
        public void onBindViewHolder(WeekAdapter.WeekViewHolder holder, int position) {
            holder.mTextView.setText(weeks.get(position));
            holder.mTextView.setOnClickListener((v)->{
                if (holder.isChoose) {
                    holder.mTextView.setBackgroundResource(R.drawable.circle_text_normal);
                    holder.mTextView.setTextColor(Color.parseColor("#595959"));
                    mWeeks.remove(position+1);
                    holder.isChoose = false;
                }else{
                    holder.mTextView.setTextColor(Color.parseColor("#ffffff"));
                    holder.mTextView.setBackgroundResource(R.drawable.circle_text_pressed);
                    mWeeks.add(position+1);
                    holder.isChoose = true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return weeks.size();
        }

        class WeekViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.item_tv_choose_week)
            TextView mTextView;
            private boolean isChoose = false;
            private RelativeLayout layout;

            public boolean isChoose() {
                return isChoose;
            }

            public void setChoose(boolean choose) {
                isChoose = choose;
            }

            public WeekViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.item_tv_choose_week);
            }
        }
    }
}
