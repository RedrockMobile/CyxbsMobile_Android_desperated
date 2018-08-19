package com.mredrock.cyxbs.freshman.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.StrategyData;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReportingProcessMoreActivity extends BaseActivity implements View.OnClickListener {

    private StrategyData.DetailData mData;
    private RoundedImageView real;
    private RoundedImageView map;

    private String realStr;
    private String mapStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mData = (StrategyData.DetailData) intent.getSerializableExtra("data");

        initView();
    }

    private void initView() {
        real = findViewById(R.id.riv_report_real_more);
        map = findViewById(R.id.riv_report_map_more);
        TextView title = findViewById(R.id.tv_report_location_more);
        TextView step = findViewById(R.id.tv_report_step_more);
        TextView context = findViewById(R.id.tv_report_context_more);
        ImageView arrow = findViewById(R.id.iv_report_arrow_more);

        real.setOnClickListener(this);
        map.setOnClickListener(this);
        context.setOnClickListener(this);
        arrow.setOnClickListener(this);

        title.setText(mData.getName());
        String temp = "步骤" + mData.getId();
        step.setText(temp);
        realStr = "http://47.106.33.112:8080/welcome2018" + mData.getPicture().get(0);
        mapStr = "http://47.106.33.112:8080/welcome2018" + mData.getPicture().get(1);
        Glide.with(this)
                .load(realStr)
                .asBitmap()
                .thumbnail(0.2f)
                .placeholder(R.drawable.freshman_preload_img)
                .into(new BitmapImageViewTarget(real) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        real.setImageBitmap(resource);
                    }
                });
        Glide.with(this)
                .load(mapStr)
                .asBitmap()
                .thumbnail(0.2f)
                .placeholder(R.drawable.freshman_preload_img)
                .into(new BitmapImageViewTarget(map) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        map.setImageBitmap(resource);
                    }
                });
        context.setText(mData.getContent());
    }

    @Override
    public void onClick(View v) {
        List<String> url = new ArrayList<>();
        url.add(realStr);
        url.add(mapStr);
        int i = v.getId();
        if (i == R.id.riv_report_map_more) {
            PhotoViewerActivityKt.start(App.getContext(), url, 1);

        } else if (i == R.id.riv_report_real_more) {
            PhotoViewerActivityKt.start(App.getContext(), url, 0);

        } else if (i == R.id.tv_report_context_more || i == R.id.iv_report_arrow_more) {
            finish();

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.freshman_anim_in, R.anim.freshman_anim_out);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.freshman_activity_reporting_process_more;
    }

    @NotNull
    @Override
    public String getToolbarTitle() {
        return Const.INDEX_REGISTRATION;
    }
}
