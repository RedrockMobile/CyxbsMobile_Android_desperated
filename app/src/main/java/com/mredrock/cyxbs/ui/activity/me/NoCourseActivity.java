package com.mredrock.cyxbs.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbs.model.Student;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.NoCourseAdapter;
import com.mredrock.cyxbs.ui.fragment.me.NoCourseItemFragment;
import com.mredrock.cyxbs.util.NetUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoCourseActivity extends BaseActivity
        implements View.OnClickListener,
        NoCourseAdapter.OnItemButtonClickListener {

    public static final int    REQUEST_SELECT  = 1;
    public static final String EXTRA_NO_COURSE = "extra_no_course";

    @Bind(R.id.no_course_stu)
    EditText     noCourseStu;
    @Bind(R.id.no_course_add)
    TextView     noCourseAdd;
    @Bind(R.id.no_course_have)
    TextView     noCourseHave;
    @Bind(R.id.no_course_change)
    TextView     noCourseChange;
    @Bind(R.id.no_course_recycler_view)
    RecyclerView noCourseRecyclerView;
    @Bind(R.id.no_course_search)
    LinearLayout noCourseSearch;
    @Bind(R.id.toolbar_title)
    TextView     toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar      toolbar;

    private ArrayList<String> stuNumList;
    private ArrayList<String> nameList;
    private NoCourseAdapter   mNoCourseAdapter;

    private int count = 0;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_course);
        ButterKnife.bind(this);
        initToolbar();
        init();
    }


    private void init() {
        noCourseAdd.setOnClickListener(this);
        noCourseSearch.setOnClickListener(this);
        noCourseChange.setOnClickListener(this);
        stuNumList = new ArrayList<>();
        nameList = new ArrayList<>();

        mNoCourseAdapter = new NoCourseAdapter(nameList);
        mNoCourseAdapter.setOnItemButtonClickListener(this);

        if (APP.isLogin()) {
            mUser = APP.getUser(this);
        }
        if (mUser != null) {
            addStudent(mUser.stuNum, mUser.name);
        }

        noCourseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noCourseRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        noCourseRecyclerView.setAdapter(mNoCourseAdapter);

        noCourseStu.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {

                        }
                        return true;
                    }
                });
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("没课约");
            setSupportActionBar(toolbar);
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
            case R.id.no_course_add:
                if (!noCourseStu.getText().toString().equals("")) {
                    doAddAction();
                }
                break;
            case R.id.no_course_search:
                Intent intent = new Intent(this, NoCourseContainerActivity
                        .class);
                intent.putStringArrayListExtra(NoCourseItemFragment
                        .EXTRA_NAME_LIST, nameList);
                intent.putStringArrayListExtra(NoCourseItemFragment
                        .EXTRA_STU_NUM_LIST, stuNumList);
                startActivity(intent);
                break;
            case R.id.no_course_change:
                if (noCourseChange.getText().toString().equals("修改")) {
                    noCourseChange.setText("完成");
                    mNoCourseAdapter.setButtonVisible();
                } else {
                    noCourseChange.setText("修改");
                    mNoCourseAdapter.setButtonInVisible();
                    mNoCourseAdapter = null;
                    mNoCourseAdapter = new NoCourseAdapter(nameList);
                    noCourseRecyclerView.setAdapter(mNoCourseAdapter);
                    mNoCourseAdapter.setOnItemButtonClickListener(this);
                }
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
                Snackbar.make(noCourseStu, "请不要重复添加！", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }


    private void addStudent(String stuNum, String name) {
        stuNumList.add(stuNum);
        nameList.add(name);
        count++;
        StringBuilder sb = new StringBuilder();
        noCourseHave.setText(sb.append("已添加").append(count).append("人"));
        mNoCourseAdapter.notifyDataSetChanged();
    }


    private void removeStudent(int position) {
        stuNumList.remove(
                stuNumList.remove((stuNumList.size() - 1) - position));
        noCourseHave.setText(
                new StringBuilder("已添加").append(count).append("人"));
    }


    private void doAddAction() {
        if (stuNumList.contains(noCourseStu.getText().toString())) {
            Snackbar.make(noCourseStu, "请不要重复添加！", Snackbar.LENGTH_SHORT)
                    .show();
        } else if (!NetUtils.isNetWorkAvailable(this)) {
            Snackbar.make(noCourseStu, "没有网络连接，无法查找该同学！", Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            RequestManager.INSTANCE.getStudent(
                    new SimpleSubscriber<>(this, true,
                            new SubscriberListener<List<Student>>() {
                                @Override
                                public void onNext(List<Student> students) {
                                    super.onNext(students);
                                    if (students != null &&
                                            students.size() != 0) {
                                        Pattern pattern = Pattern.compile(
                                                "[0-9]*");
                                        Matcher m = pattern.matcher(
                                                noCourseStu.getText().toString());
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
                                            noCourseStu.setHint(
                                                    "输入学号/姓名可以继续添加");
                                            noCourseStu.setText("");
                                        }
                                    } else {
                                        Snackbar.make(noCourseStu, "没有找到这个人哦~",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }), noCourseStu.getText().toString());
        }
    }

    @Override
    public void onClickEnd(int position) {
        removeStudent(position);
        count--;
        noCourseHave.setText(new StringBuilder("已添加").append(count).append("人"));
        if (nameList.size() == 0) {
            noCourseStu.setHint("请输入要查询的学号/姓名");
            noCourseChange.setText("修改");
            mNoCourseAdapter.setButtonInVisible();
            mNoCourseAdapter = null;
            mNoCourseAdapter = new NoCourseAdapter(nameList);
            noCourseRecyclerView.setAdapter(mNoCourseAdapter);
            mNoCourseAdapter.setOnItemButtonClickListener(this);
        }
    }
}
