package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.NineGridlayout;
import com.mredrock.cyxbsmobile.model.Image;
import com.mredrock.cyxbsmobile.util.ScreenTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.Observable;

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
    @Bind(R.id.iv_ngrid_layout)
    NineGridlayout mNineGridlayout;

    private final static int REQUEST_IMAGE = 0001;
    private List<Image> mImgs;
    private int singlePicX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mImgs = new ArrayList<>();
        singlePicX = (int) new ScreenTools(this).getSinglePicX();
        mImgs.add(new Image("file:///android_asset/add_news.jpg", 500, 500, Image.ADDIMAG));
        mNineGridlayout.setImagesData(mImgs);
        setSupportActionBar(mToolBar);
        mCancelText.setOnClickListener(this);
        mSaveText.setOnClickListener(this);
        mNineGridlayout.setOnAddImagItemClickListener((v, position) -> {
            Intent intent = new Intent(AddNewsActivity.this, MultiImageSelectorActivity.class);
            // 是否显示调用相机拍照
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
            // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
            // 默认选择图片,回填选项(支持String ArrayList)
            startActivityForResult(intent, REQUEST_IMAGE);
        });
        mNineGridlayout.setmOnClickDeletecteListener(new NineGridlayout.OnClickDeletecteListener() {
            @Override
            public void onClickDelete(View v, int position) {
                Log.e("==============>>>>>>", position + "");
                mImgs.remove(position);
                mNineGridlayout.setImagesData(mImgs);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_cancel:
                AddNewsActivity.this.finish();
                break;
            case R.id.toolbar_save:
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

                Observable.from(path)
                        .map(s -> new Image(s, singlePicX, singlePicX, Image.NORMALIMAGE))
                        .map(image -> {
                            mImgs.add(image);
                            return mImgs;
                        })
                        .subscribe(images -> {
                            mNineGridlayout.setImagesData(images);
                        }, throwable -> {
                            Toast.makeText(AddNewsActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }
}
