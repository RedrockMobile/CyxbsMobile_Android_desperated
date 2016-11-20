package com.mredrock.cyxbs.ui.activity.affair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Position;
import com.mredrock.cyxbs.event.AffairAddEvent;
import com.mredrock.cyxbs.event.TimeChooseEvent;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.KeyboardUtils;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.StatusBarUtil;
import com.mredrock.cyxbs.util.database.DBManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mredrock.cyxbs.util.LogUtils.LOGE;


public class EditAffairActivity extends AppCompatActivity {

    public static  final String BUNDLE_KEY = "position";
    public static final String WEEK_NUMBER = "week";
    private static final String COURSE_KEY = "course";
    private final String[] TIMES = new String[]{"不提醒", "提前5分钟", "提前10分钟", "提前20分钟", "提前30分钟", "提前一个小时"};
    private final int[] TIME_MINUTE = new int[]{0, 5, 10, 20, 30, 60};

    private final String[] WEEKS = {"周一","周二","周三","周四","周五","周六","周日"};
    private final String[] CLASSES = {"一二节","三四节","五六节","七八节","九十节","AB节"};
    BottomSheetBehavior behavior;


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
    private int time = 0;


    @OnClick(R.id.edit_affair_remind_layout)
    public void onRemindTimeClick(View v){
        KeyboardUtils.hideInput(v);
        new AlertDialog.Builder(this).setTitle("选择提醒时间")
                .setItems(TIMES,(dialog,i)->{
                    mRemindTimeText.setText(TIMES[i]);
                    time = TIME_MINUTE[i];
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

    @OnClick({R.id.edit_affair_iv_save,R.id.edit_affair_iv_back})
    public void onSaveClick(View v){
        KeyboardUtils.hideInput(v);
        if (v.getId() == R.id.edit_affair_iv_save){
            String title = mTitleEdit.getText().toString();
            String content = mContentEdit.getText().toString();
            if (title.trim().isEmpty() || content.trim().isEmpty()){
                Toast.makeText(APP.getContext(),"标题和内容不能为空哦",Toast.LENGTH_SHORT).show();

            }else if(weeks.size() == 0 || positions.size() == 0){
                Toast.makeText(APP.getContext(),"时间或周数不能为空哦",Toast.LENGTH_SHORT).show();
            } else {
                DBManager dbManager = DBManager.INSTANCE;
                Affair affair = new Affair();
                Observable<Boolean> observable = Observable.create((subscriber)->{

                    Gson g = new Gson();
                    for (int i = 0; i < positions.size(); i++) {
                        int x;//定义两变量
                        Random ne=new Random();//实例化一个random的对象ne
                        x=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999

                        affair.uid = System.currentTimeMillis() +"" +x;

                        affair.hash_day = positions.get(i).getX();
                        affair.hash_lesson = positions.get(i).getY();
                        affair.period = 2 ;
                        affair.course = title;
                        affair.teacher = content;
                        affair.classroom =" ";
                        affair.begin_lesson = affair.hash_lesson * 2 +  1;
                        affair.type = "提醒";
                        affair.time = time;
                        affair.week = weeks;
                        affair.courseType = 2;
                        affair.rawWeek = " ";
                        subscriber.onNext(dbManager.insert(affair.uid, APP.getUser(this).stuNum,g.toJson(affair)));
                    }
                    subscriber.onCompleted();
                    dbManager.close();

                });
                SimpleSubscriber<Boolean> subscriber = new SimpleSubscriber<Boolean>(this, false, new SubscriberListener<Boolean>() {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        LOGE("onCompleted()","EventBus.getDefault().post(new AffairAddEvent(affair));");
                        EventBus.getDefault().post(new AffairAddEvent(affair));
                        dbManager.close();
                        onBackPressed();
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        Toast.makeText(APP.getContext(),"添加失败，请重试！",Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                    }
                });
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
            }
        }else {
            onBackPressed();
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
        initView();
        initData();



    }

    private void initData() {

        Position position = (Position) getIntent().getSerializableExtra(BUNDLE_KEY);
        if (position != null){
            positions.add(position);
            mTimeChooseText.setText(WEEKS[position.getX()]+CLASSES[position.getY()]);
        }

        int currentWeek = getIntent().getIntExtra(WEEK_NUMBER,-1);
        if (currentWeek != -1){
            mWeekAdapter.addWeekNum(currentWeek);
            onWeekChooseOkClick();
        }
    }

    private void initView() {
        behavior = BottomSheetBehavior.from(findViewById(R.id.scroll));
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mTitleEdit.setOnFocusChangeListener((view, b) -> {
            if (b)
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
        mContentEdit.setOnFocusChangeListener((view, b) -> {
            if (b)
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mWeekAdapter = new WeekAdapter();
        mRecyclerView.setAdapter(mWeekAdapter);
    }

    public static void editAffairActivityStart(Context context,int weekNum) {
        Intent starter = new Intent(context, EditAffairActivity.class);
        starter.putExtra(WEEK_NUMBER,weekNum);
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

       public void addWeekNum(int weekNum){
           LOGE("addWeekNum",weekNum+" ");
           mWeeks.add(weekNum);
       }

        @Override
        public void onBindViewHolder(WeekAdapter.WeekViewHolder holder, int position) {
            holder.mTextView.setBackgroundResource(R.drawable.circle_text_normal);
            holder.mTextView.setTextColor(Color.parseColor("#595959"));
            LOGE("onBindViewHolder position",position+" ");
            holder.isChoose = false;
            holder.mTextView.setText(weeks.get(position));
            if (mWeeks.contains(position + 1)){
                LOGE("mWeeks.contains(position)",position+"   ");
                holder.mTextView.setTextColor(Color.parseColor("#ffffff"));
                holder.mTextView.setBackgroundResource(R.drawable.circle_text_pressed);
                holder.isChoose = true;
            }
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
