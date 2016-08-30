package com.excitingboat.freshmanspecial.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.excitingboat.freshmanspecial.R;

public class FreshmanMainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_freshman_special__activity_freshman_main);
        init();
    }

    private void init() {
        findViewById(R.id.bt_toolbar_back).setOnClickListener(this);
        findViewById(R.id.freshman_guide_card).setOnClickListener(this);
        findViewById(R.id.freshman_big_data_card).setOnClickListener(this);
        findViewById(R.id.freshman_cqupt_card).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_toolbar_title);
        title.setText("2016迎新网");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.freshman_guide_card) {
            startActivity(new Intent(this, FreshmenGuideActivity.class));

        } else if (i == R.id.freshman_big_data_card) {
            startActivity(new Intent(this, FreshmenBigDataActivity.class));

        } else if (i == R.id.freshman_cqupt_card) {
            startActivity(new Intent(this, FreshmenCQUPTStyleActivity.class));

        } else if (i == R.id.bt_toolbar_back) {
            finish();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, FreshmanMainActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }
}
