package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;

public class EditInfoActivity extends BaseActivity
        implements View.OnClickListener {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edit_info_avatar_layout)
    RelativeLayout editInfoAvatarLayout;
    @Bind(R.id.edit_info_nick_layout)
    RelativeLayout editInfoNickLayout;
    @Bind(R.id.edit_info_introduce_layout)
    RelativeLayout
            editInfoIntroduceLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        initToolbar();
        editInfoAvatarLayout.setOnClickListener(this);
        editInfoIntroduceLayout.setOnClickListener(this);
        editInfoNickLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info_avatar_layout:

                break;
            case R.id.edit_info_nick_layout:
                startActivity(new Intent(this, EditNickNameActivity.class));
                break;
            case R.id.edit_info_introduce_layout:
                startActivity(new Intent(this, EditIntroduceActivity.class));
                break;
        }
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("修改信息");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> EditInfoActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
