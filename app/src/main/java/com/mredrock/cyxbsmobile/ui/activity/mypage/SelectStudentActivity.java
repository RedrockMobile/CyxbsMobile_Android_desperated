package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbsmobile.model.Student;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.adapter.SelectStudentAdapter;

import java.util.List;


public class SelectStudentActivity extends BaseActivity {

    public static final String EXTRA_STUDENT_LIST = "extra_student_list";

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.select_recycler_view)
    RecyclerView selectRecyclerView;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);
        ButterKnife.bind(this);
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
