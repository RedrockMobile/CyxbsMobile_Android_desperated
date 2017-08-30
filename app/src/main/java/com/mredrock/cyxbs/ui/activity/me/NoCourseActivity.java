package com.mredrock.cyxbs.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Student;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.NoCourseAdapter;
import com.mredrock.cyxbs.ui.fragment.me.NoCourseItemFragment;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.NetUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoCourseActivity extends BaseActivity
        implements View.OnClickListener,
        NoCourseAdapter.OnItemButtonClickListener {

    public static final int REQUEST_SELECT = 1;
    public static final String EXTRA_NO_COURSE = "extra_no_course";

    @BindView(R.id.no_course_recycler_view)
    RecyclerView noCourseRecyclerView;
    @BindView(R.id.query)
    Button noCourseSearch;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<String> stuNumList;
    private ArrayList<String> nameList;
    private NoCourseAdapter mNoCourseAdapter;

    private User mUser;


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_course);
        ButterKnife.bind(this);
        initToolbar();
        init();
    }


    private void init() {
        noCourseSearch.setOnClickListener(this);
        stuNumList = new ArrayList<>();
        nameList = new ArrayList<>();

        mNoCourseAdapter = new NoCourseAdapter(this, nameList);
        mNoCourseAdapter.setOnItemButtonClickListener(this);

        if (APP.isLogin()) {
            mUser = APP.getUser(this);
        }
        if (mUser != null) {
            addStudent(mUser.stuNum, mUser.name);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                Math.max(1, DensityUtils.getScreenWidth(this) / DensityUtils.dp2px(this, 86)));
        noCourseRecyclerView.setLayoutManager(layoutManager);
        /*noCourseRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));*/
        noCourseRecyclerView.setAdapter(mNoCourseAdapter);
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("没课约");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(v -> NoCourseActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query:
                Intent intent = new Intent(this, NoCourseContainerActivity
                        .class);
                intent.putStringArrayListExtra(NoCourseItemFragment
                        .EXTRA_NAME_LIST, nameList);
                intent.putStringArrayListExtra(NoCourseItemFragment
                        .EXTRA_STU_NUM_LIST, stuNumList);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT && resultCode == RESULT_OK) {
            Student student = (Student) data.getExtras().getSerializable(EXTRA_NO_COURSE);
            if (!stuNumList.contains(student.stunum)) {
                addStudent(student.stunum, student.name);
            } else {
                Snackbar.make(noCourseSearch, "请不要重复添加！", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }


    private void addStudent(String stuNum, String name) {
        stuNumList.add(stuNum);
        nameList.add(name);
        mNoCourseAdapter.notifyDataSetChanged();
    }


    private void removeStudent(int position) {
        stuNumList.remove(
                stuNumList.remove((stuNumList.size() - 1) - position));
    }


    public void doAddAction(String stu) {
        if (stuNumList.contains(stu)) {
            Snackbar.make(noCourseSearch, "请不要重复添加！", Snackbar.LENGTH_SHORT)
                    .show();
        } else if (!NetUtils.isNetWorkAvailable(this)) {
            Snackbar.make(noCourseSearch, "没有网络连接，无法查找该同学！", Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            RequestManager.INSTANCE.getStudent(
                    new SimpleObserver<>(this, true,
                            new SubscriberListener<List<Student>>() {
                                @Override
                                public void onNext(List<Student> students) {
                                    super.onNext(students);
                                    if (students != null &&
                                            students.size() != 0) {
                                        Pattern pattern = Pattern.compile(
                                                "[0-9]*");
                                        Matcher m = pattern.matcher(stu);
                                        //输入的不是数字
                                        if (!m.matches()) {
                                            Intent intent = new Intent(
                                                    NoCourseActivity.this,
                                                    SelectStudentActivity.class);
                                            intent.putExtra(
                                                    SelectStudentActivity.EXTRA_STUDENT_LIST,
                                                    (Serializable) students);
                                            startActivityForResult(intent, REQUEST_SELECT);
                                        } else {
                                            addStudent(students.get(0).stunum,
                                                    students.get(0).name);
                                        }
                                    } else {
                                        Snackbar.make(noCourseSearch, "没有找到这个人哦~",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public boolean onError(Throwable e) {
                                    if (e instanceof JsonSyntaxException) {
                                        Snackbar.make(noCourseSearch, "没有找到这个人哦~",
                                                Snackbar.LENGTH_SHORT).show();
                                        return true;
                                    }
                                    return false;
                                }
                            }), stu);
        }
    }

    @Override
    public void onClickEnd(int position) {
        removeStudent(position);
        if (nameList.size() == 0) {
            mNoCourseAdapter = null;
            mNoCourseAdapter = new NoCourseAdapter(this, nameList);
            noCourseRecyclerView.setAdapter(mNoCourseAdapter);
            mNoCourseAdapter.setOnItemButtonClickListener(this);
        }
    }
}
