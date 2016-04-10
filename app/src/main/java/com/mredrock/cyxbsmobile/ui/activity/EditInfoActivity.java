package com.mredrock.cyxbsmobile.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;

public class EditInfoActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Bind(R.id.edit_info_iv_back) ImageView editInfoIvBack;
    @Bind(R.id.edit_info_avatar_layout) RelativeLayout editInfoAvatarLayout;
    @Bind(R.id.edit_info_nick_layout) RelativeLayout editInfoNickLayout;
    @Bind(R.id.edit_info_introduce_layout) RelativeLayout
            editInfoIntroduceLayout;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        editInfoIvBack.setOnClickListener(this);
        editInfoAvatarLayout.setOnClickListener(this);
        editInfoIntroduceLayout.setOnClickListener(this);
        editInfoNickLayout.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info_iv_back:
                finish();
                break;
        }
    }
}
