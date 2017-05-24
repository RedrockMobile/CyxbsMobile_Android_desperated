package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbs.model.Student;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.SelectStudentAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SelectStudentActivity extends BaseActivity {

    public static final String EXTRA_STUDENT_LIST = "extra_student_list";

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.select_recycler_view)
    RecyclerView selectRecyclerView;


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


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this, 50);
        initToolbar();
        List<Student> studentList = (List<Student>) getIntent().getSerializableExtra(
                EXTRA_STUDENT_LIST);
        SelectStudentAdapter adapter = new SelectStudentAdapter(studentList, this);
        selectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        selectRecyclerView.setAdapter(adapter);
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("选择");
            toolbar.setNavigationIcon(R.drawable.back);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> SelectStudentActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

}
