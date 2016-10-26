package com.mredrock.cyxbs.ui.activity.affair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mredrock.cyxbs.R.id.add_affair_title_edit;
import static com.mredrock.cyxbs.R.id.cancel;
import static com.mredrock.cyxbs.R.string.course;
import static com.mredrock.cyxbs.R.string.find;
import static com.mredrock.cyxbs.R.string.preview;
import static java.util.Arrays.asList;
import static u.aly.av.O;
import static u.aly.av.b;
import static u.aly.av.f;
import static u.aly.av.h;
import static u.aly.av.l;
import static u.aly.av.m;
import static u.aly.av.p;
import static u.aly.av.w;

public class EditAffairActivity extends AppCompatActivity {

    private static final String COURSE_KEY = "course";
    BottomSheetBehavior behavior;

    @Bind(R.id.add_affair_title_edit)
    EditText mEditTitle;
    @Bind(R.id.add_affair_content_edit)
    EditText mEditContent;

    @Bind(R.id.affair_list_choose_week)
    RecyclerView mRecyclerView;

    @Bind(R.id.cancel)
    TextView cancelText;

    @Bind(R.id.save)
    TextView saveText;

    @Bind(R.id.title)
    TextView titleText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_affair);
        ButterKnife.bind(this);
        //intro();
        behavior = BottomSheetBehavior.from(findViewById(R.id.scroll));


        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        findViewById(R.id.add_affair_remind_time).setOnClickListener((v -> intro()));
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
        titleText.setText("添加备忘");
        cancelText.setOnClickListener((view -> onBackPressed()));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new WeekAdapter());
        mRecyclerView.smoothScrollToPosition(10);
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
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }

    class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {
        private List<String> weeks = new ArrayList<>();


        public WeekAdapter() {
            weeks.addAll(Arrays.asList(EditAffairActivity.this.getResources().getStringArray(R.array.titles_weeks)));
            weeks.remove(0);
        }

        @Override
        public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeekViewHolder(LayoutInflater.from(EditAffairActivity.this).inflate(R.layout.item_choose_week, parent, false));
        }

        @Override
        public void onBindViewHolder(WeekViewHolder holder, int position) {
            holder.mTextView.setText(weeks.get(position));
        }

        @Override
        public int getItemCount() {
            return weeks.size();
        }

        class WeekViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.item_tv_choose_week)
            TextView mTextView;
            private boolean isChoose = false;
            private CardView mCardView;

            @OnClick(R.id.item_tv_choose_week)
            public void chooseWeekClick() {

            }

            public WeekViewHolder(View itemView) {
                super(itemView);
                this.mCardView = (CardView) itemView;
                ButterKnife.bind(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.item_tv_choose_week);
                mTextView.setOnClickListener((v)->{
                    if (isChoose) {
                        mCardView.setCardBackgroundColor(Color.WHITE);
                        isChoose = false;
                    }else{
                        mCardView.setCardBackgroundColor(Color.parseColor("#00aae8"));
                        isChoose = true;
                    }
                });
            }


        }
    }


}
