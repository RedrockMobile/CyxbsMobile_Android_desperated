package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AddNewsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar_cancel)
    TextView mCancelText;
    @Bind(R.id.toolbar_title)
    TextView mTitleText;
    @Bind(R.id.toolbar_save)
    TextView mSaveText;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.add_news_edit)
    EditText mAddNewsEdit;
    @Bind(R.id.add_news_img)
    ImageView mAddImageView;
    private final static int REQUEST_IMAGE = 0001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(mToolBar);
        mCancelText.setOnClickListener(this);
        mSaveText.setOnClickListener(this);
        mAddImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_cancel:
                AddNewsActivity.this.finish();
                break;
            case R.id.toolbar_save:
                break;
            case R.id.add_news_img:
                Intent intent = new Intent(AddNewsActivity.this, MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                // 默认选择图片,回填选项(支持String ArrayList)
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
            }
        }
    }
}
