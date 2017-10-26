package com.mredrock.cyxbs.ui.activity.affair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Position;
import com.mredrock.cyxbs.event.AffairAddEvent;
import com.mredrock.cyxbs.event.AffairModifyEvent;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.AffairApi;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.network.exception.RedrockApiException;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.PickerBottomSheetDialog;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.database.DBManager;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EditAffairActivity extends BaseActivity {

    private static final String TAG = "EditAffairActivity";

    public static final String BUNDLE_KEY = "position";
    public static final String WEEK_NUMBER = "week";
    private static final String COURSE_KEY = "course";
    private final String[] TIMES = new String[]{"不提醒", "提前5分钟", "提前10分钟", "提前20分钟", "提前30分钟", "提前一个小时"};
    private final int[] TIME_MINUTE = new int[]{0, 5, 10, 20, 30, 60};

    private final String[] WEEKS = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private final String[] CLASSES = {"1~2节", "3~4节", "5~6节", "7~8节", "9~10节", "11~12节"};
    private boolean isStartByCourse = false;
    private String uid;

    @BindView(R.id.remind_text)
    TextView mRemindText;
    @BindView(R.id.week_text)
    TextView mWeekText;
    @BindView(R.id.time_text)
    TextView mTimeText;
    @BindView(R.id.content)
    EditText mContentEdit;
    @BindView(R.id.title)
    EditText mTitleEdit;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    private List<Integer> weeks = new ArrayList<>();
    private WeekAdapter mWeekAdapter;
    private ArrayList<Position> mPositions = new ArrayList<>();
    private int time = 0;
    private BottomSheetDialog mPickWeekDialog;
    private PickerBottomSheetDialog mPickRemindDialog;
    private BottomSheetDialog mPickTimeDialog;

    @OnClick(R.id.choose_remind)
    public void showChooseRemindDialog() {
        if (mPickRemindDialog == null) {
            initPickRemindDialog();
        }
        mPickRemindDialog.show();
    }

    @OnClick(R.id.choose_week)
    public void showChooseWeekDialog() {
        if (mPickWeekDialog == null) {
            initPickWeekDialog();
        }
        mPickWeekDialog.show();
    }

    @OnClick(R.id.choose_time)
    public void showChooseTimeDialog() {
        if (mPickTimeDialog == null) {
            initPickTimeDialog();
        }
        mPickTimeDialog.show();
/*
        Intent i = new Intent(this, TimeChooseActivity.class);
        i.putExtra(TimeChooseActivity.BUNDLE_KEY, mPositions);
        startActivity(i);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_affair);
        ButterKnife.bind(this);
        initView();

        if (!initData())
            initCourse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_affair, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit) {
            submit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initCourse() {
        Affair course = getIntent().getParcelableExtra(COURSE_KEY);
        if (course == null)
            return;
        uid = course.uid;
        isStartByCourse = true;
        //不知道有啥用，注释掉再说
        /*setData(course);*/
        DBManager.INSTANCE.queryItem(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AffairApi.AffairItem>() {
                    @Override
                     public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AffairApi.AffairItem affairItem) {
                        if (affairItem != null) {
                            int color = Color.parseColor("#666666");
                            mTitleEdit.setText(affairItem.getTitle());
                            mContentEdit.setText(affairItem.getContent());
                            mRemindText.setTextColor(color);
                            mRemindText.setText(TIMES[transferTimeToText(course.time)]);

                            StringBuilder builder = new StringBuilder("第");
                            List<Integer> weekList = affairItem.getDate().get(0).getWeek();
                            Collections.sort(weekList);
                            mWeekAdapter.addAllWeekNum(weekList);
                            for (int week : weekList) {
                                builder.append(week)
                                        .append(",");
                                if (!weeks.contains(week)) {
                                    weeks.add(week);
                                }
                            }
                            mWeekText.setTextColor(color);
                            mWeekText.setText(builder.deleteCharAt(builder.length() - 1)
                                    .append("周").toString());

                            builder.delete(0, builder.length());
                            for (AffairApi.AffairItem.DateBean dateBean : affairItem.getDate()) {
                                Position position = new Position(dateBean.getDay(), dateBean.getClassX());
                                if (!mPositions.contains(position)) {
                                    mPositions.add(position);
                                }
                                builder.append(WEEKS[position.getX()])
                                        .append(CLASSES[position.getY()])
                                        .append("，");
                            }
                            mTimeText.setTextColor(color);
                            mTimeText.setText(builder.deleteCharAt(builder.length() - 1).toString());
                        }

                    }
                });
    }

    /*
        private void setData(Affair course) {
            mTitleEdit.setText(course.course);
            mContentEdit.setText(course.teacher);
            mRemindText.setText(TIMES[transferTimeToText(course.time)]);

            mWeekAdapter.addAllWeekNum(course.week);

            Position position = new Position(course.hash_day, course.hash_lesson);
            mPositions.add(position);
            StringBuilder builder = new StringBuilder();
            mTimeText.setText(builder.toString());
        }
    */
    private boolean initData() {
        Position position = (Position) getIntent().getSerializableExtra(BUNDLE_KEY);
        if (position != null) {
            mPositions.add(position);
            mTimeText.setText(WEEKS[position.getX()] + CLASSES[position.getY()]);
            mTimeText.setTextColor(Color.parseColor("#666666"));
        }
        int currentWeek = getIntent().getIntExtra(WEEK_NUMBER, -1);
        if (currentWeek > 0 && currentWeek <= 18) {
            mWeekAdapter.addWeekNum(currentWeek);
            weeks.add(currentWeek);
            mWeekText.setText("第" + currentWeek + "周");
            mWeekText.setTextColor(Color.parseColor("#666666"));
            return true;
        }
        return false;
    }

    private void initView() {
        initToolbar();
        mWeekAdapter = new WeekAdapter();
    }

    private void initPickTimeDialog() {
        mPickTimeDialog = new BottomSheetDialog(this);
        View itemView = LayoutInflater.from(this).inflate(R.layout.dialog_pick_time, null, false);
        mPickTimeDialog.setContentView(itemView);

        itemView.findViewById(R.id.divider).setVisibility(View.GONE);
        GridView gridView = (GridView) itemView.findViewById(R.id.gridView);
        int numCol = 7;
        int size = (DensityUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 35 + 6)) / 7;
        ArrayList<Position> positions = new ArrayList<>(mPositions);
        gridView.setNumColumns(numCol);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 7 * CLASSES.length;
            }

            @Override
            public Object getItem(int position) {
                return positions.contains(new Position(position % numCol, position / numCol));
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CardView cardView = new CardView(EditAffairActivity.this);
                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT, size);
                cardView.setLayoutParams(layoutParams);
                Position position1 = new Position(position % numCol, position / numCol);
                if (positions.contains(position1)) {
                    cardView.setBackgroundResource(R.drawable.shape_rectangle_blue_fill);
                    cardView.setCardElevation(DensityUtils.dp2px(parent.getContext(), 2));
                } else {
                    cardView.setCardElevation(0);
                    cardView.setBackgroundColor(Color.WHITE);
                }
                return cardView;
            }
        });
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            CardView cardView = (CardView) view;
            Position position1 = new Position(position % numCol, position / numCol);
            if (positions.contains(position1)) {
                cardView.setBackgroundColor(Color.WHITE);
                cardView.setCardElevation(0);
                positions.remove(position1);
            } else {
                cardView.setBackgroundResource(R.drawable.shape_rectangle_blue_fill);
                cardView.setCardElevation(DensityUtils.dp2px(this, 2));
                positions.add(position1);
            }
        });

        itemView.findViewById(R.id.cancel).setOnClickListener(v -> {
            positions.clear();
            positions.addAll(mPositions);
            mPickTimeDialog.dismiss();
        });
        itemView.findViewById(R.id.sure).setOnClickListener(v -> {
            mPositions.clear();
            mPositions.addAll(positions);
            Collections.sort(mPositions);
            StringBuilder builder = new StringBuilder();
            for (Position position : mPositions) {
                builder.append(WEEKS[position.getX()]);
                builder.append(CLASSES[position.getY()]);
                builder.append("，");
            }
            if (mPositions.isEmpty()) {
                mTimeText.setTextColor(Color.parseColor("#999999"));
                mTimeText.setText("选择时间");
            } else {
                mTimeText.setTextColor(Color.parseColor("#666666"));
                mTimeText.setText(builder.deleteCharAt(builder.length() - 1).toString());
            }
            mPickTimeDialog.dismiss();
        });
    }

    private void initPickWeekDialog() {
        mPickWeekDialog = new BottomSheetDialog(this);
        View itemView = LayoutInflater.from(this).inflate(R.layout.dialog_pick_week, null, false);
        int count = Math.max(1, (DensityUtils.getScreenWidth(this)
                - DensityUtils.dp2px(this, 20)) / DensityUtils.dp2px(this, 100));
        GridLayoutManager layoutManager = new GridLayoutManager(this, count);
        layoutManager.setAutoMeasureEnabled(true);
        RecyclerView rv = (RecyclerView) itemView.findViewById(R.id.recyclerView);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(mWeekAdapter);
        View cancel = itemView.findViewById(R.id.cancel);
        View sure = itemView.findViewById(R.id.sure);
        cancel.setOnClickListener(v -> mPickWeekDialog.dismiss());
        sure.setOnClickListener(v -> {
            weeks.clear();
            weeks.addAll(mWeekAdapter.getWeekNum());
            if (weeks.size() != 0) {
                Collections.sort(weeks);
                String data = weeks.toString();
                data = data.substring(1, data.length() - 1);
                mWeekText.setText("第" + data + "周");
                mWeekText.setTextColor(Color.parseColor("#666666"));
            } else {
                mWeekText.setText("选择周数");
                mWeekText.setTextColor(Color.parseColor("#999999"));
            }
            mPickWeekDialog.dismiss();
        });
        mPickWeekDialog.setContentView(itemView);
    }

    private void initPickRemindDialog() {
        mPickRemindDialog = new PickerBottomSheetDialog(this);
        mPickRemindDialog.setData(TIMES);
        mPickRemindDialog.setOnClickListener(new PickerBottomSheetDialog.OnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String value, int position) {
                time = position;
                mRemindText.setTextColor(position == 0 ?
                        Color.parseColor("#999999") : Color.parseColor("#666666"));
                mRemindText.setText(value);
            }
        });
    }

    private void initToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbarTitle.setText("编辑备忘");
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_back);
            mToolbar.setNavigationOnClickListener(v -> EditAffairActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    public static void editAffairActivityStart(Context context, int weekNum) {
        Intent starter = new Intent(context, EditAffairActivity.class);
        starter.putExtra(WEEK_NUMBER, weekNum);
        context.startActivity(starter);
    }

    public static void editAffairActivityStart(Context context, Affair affair) {
        Intent starter = new Intent(context, EditAffairActivity.class);
        starter.putExtra(COURSE_KEY, (Parcelable) affair);
        context.startActivity(starter);
    }

    private int transferTimeToText(int time) {
        int index;
        switch (time) {
            case 5:
                index = 1;
                break;
            case 10:
                index = 2;
                break;
            case 20:
                index = 3;
                break;
            case 30:
                index = 4;
                break;
            case 60:
                index = 5;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }

    //不知道啥用，删了~
/*
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeChooseEvent(TimeChooseEvent event) {
        mPositions.clear();
        mPositions.addAll(event.getPositions());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mPositions.size() && i < 3; i++) {
            stringBuffer.append(WEEKS[mPositions.get(i).getX()] + CLASSES[mPositions.get(i).getY()] + " ");
        }
        mTimeText.setText(stringBuffer.toString());
    }
*/

    class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {
        private List<String> weekName = new ArrayList<>();
        private Set<Integer> weekNum = new HashSet<>();


        public WeekAdapter() {
            weekName.addAll(Arrays.asList(EditAffairActivity.this.getResources().getStringArray(R.array.titles_weeks)));
            weekName.remove(0);
        }

        @Override
        public WeekAdapter.WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeekAdapter.WeekViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_week, parent, false));
        }

        public Set<Integer> getWeekNum() {
            return weekNum;
        }

        public void addWeekNum(int weekNum) {
            this.weekNum.add(weekNum);
        }

        public void addAllWeekNum(List<Integer> weekNums) {
            weekNum.addAll(weekNums);
        }

        @Override
        public void onBindViewHolder(WeekAdapter.WeekViewHolder holder, int position) {
            holder.mTextView.setBackgroundResource(R.drawable.shape_rectangle_grey_stroke);
            holder.mTextView.setTextColor(Color.parseColor("#666666"));
            holder.isChoose = false;
            holder.mTextView.setText(weekName.get(position));
            if (weekNum.contains(position + 1)) {
                holder.mTextView.setTextColor(Color.parseColor("#ffffff"));
                holder.mTextView.setBackgroundResource(R.drawable.shape_rectangle_blue_gradient);
                holder.isChoose = true;
            }
            holder.mTextView.setOnClickListener((v) -> {
                if (holder.isChoose) {
                    holder.mTextView.setBackgroundResource(R.drawable.shape_rectangle_grey_stroke);
                    holder.mTextView.setTextColor(Color.parseColor("#666666"));
                    weekNum.remove(position + 1);
                    holder.isChoose = false;
                } else {
                    holder.mTextView.setTextColor(Color.parseColor("#ffffff"));
                    holder.mTextView.setBackgroundResource(R.drawable.shape_rectangle_blue_gradient);
                    weekNum.add(position + 1);
                    holder.isChoose = true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return weekName.size();
        }

        class WeekViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_tv_choose_week)
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

    @SuppressWarnings("unchecked")
    public void submit() {
        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        if (title.trim().isEmpty()) {
            Toast.makeText(BaseAPP.getContext(), "标题不能为空哦", Toast.LENGTH_SHORT).show();
        } else if (weeks.size() == 0 || mPositions.size() == 0) {
            Toast.makeText(BaseAPP.getContext(), "时间或周数不能为空哦", Toast.LENGTH_SHORT).show();
        } else {
            DBManager dbManager = DBManager.INSTANCE;
            Affair affair = new Affair();
            AffairApi.AffairItem affairItem = new AffairApi.AffairItem();
            Gson gson = new Gson();
            Random ne = new Random();
            String x;
            if (uid == null)
                x = System.currentTimeMillis() + "" + (ne.nextInt(9999 - 1000 + 1) + 1000);//为变量赋随机值10009999
            else {
                x = uid;
            }
            affairItem.setContent(content);
            affairItem.setTime(TIME_MINUTE[time]);
            affairItem.setId(x);
            affairItem.setTitle(title);


            for (Position p : mPositions) {
                AffairApi.AffairItem.DateBean date = new AffairApi.AffairItem.DateBean();
                date.setClassX(p.getY());
                date.setDay(p.getX());
                date.getWeek().addAll(mWeekAdapter.getWeekNum());
                affairItem.getDate().add(date);
            }
            affair.week = affairItem.getDate().get(0).getWeek();
            if (!isStartByCourse) {
                RequestManager.getInstance().addAffair(new SimpleObserver<>(this, true, false, new SubscriberListener<Object>() {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        dbManager.insert(true, x, BaseAPP.getUser(EditAffairActivity.this).stuNum, gson.toJson(affairItem))
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer() {
                                    @Override
                                     public void onComplete() {
                                        EventBus.getDefault().post(new AffairAddEvent(affair));
                                        onBackPressed();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Object o) {

                                    }
                                });
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        if (e instanceof SocketTimeoutException)
                            Toast.makeText(EditAffairActivity.this, "连接超时，检查一下网络哦", Toast.LENGTH_SHORT).show();
                        else if (e instanceof RedrockApiException)
                            Toast.makeText(EditAffairActivity.this, "服务器出了点小毛病，请稍后再试", Toast.LENGTH_SHORT).show();
                        else if (e instanceof MalformedJsonException) {

                        }
                        return true;

                    }

                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        // LOGE("EditAffairActivity",redrockApiWrapper.id);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                }), BaseAPP.getUser(this).stuNum, BaseAPP.getUser(this).idNum, x, title, content, gson.toJson(affairItem.getDate()), affairItem.getTime());
            } else {
                //  Log.e(TAG, "onSaveClick: isStartByCourse");
                RequestManager.getInstance().editAffair(new SimpleObserver<Object>(this, true, false, new SubscriberListener<Object>() {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        dbManager.insert(true, x, BaseAPP.getUser(EditAffairActivity.this).stuNum, gson.toJson(affairItem), true)
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer() {
                                    @Override
                                     public void onComplete() {
                                        EventBus.getDefault().post(new AffairModifyEvent());
                                        onBackPressed();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Object o) {

                                    }
                                });
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        if (e instanceof SocketTimeoutException)
                            Toast.makeText(EditAffairActivity.this, "连接超时，检查一下网络哦", Toast.LENGTH_SHORT).show();
                        else if (e instanceof RedrockApiException)
                            Toast.makeText(EditAffairActivity.this, "服务器出了点小毛病，请稍后再试", Toast.LENGTH_SHORT).show();
                        else if (e instanceof MalformedJsonException) {
                            Toast.makeText(EditAffairActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onError: " + e.getMessage());
                        }
                        return true;
                    }

                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        // LOGE("EditAffairActivity",redrockApiWrapper.id);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                }), BaseAPP.getUser(this).stuNum, BaseAPP.getUser(this).idNum, x, title, content, gson.toJson(affairItem.getDate()), affairItem.getTime());
            }
        }
    }
}
