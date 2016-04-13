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
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.model.community.UploadImgResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.util.ScreenTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                sendDynamic();
                break;
        }
    }

    private void sendDynamic() {
        List<Image> currentImgs = new ArrayList<>();
        currentImgs.addAll(mImgs);
        currentImgs.remove(0);
        final String[] photosrcs = {""};
        final String[] thumbnail_srcs = {""};
        Observable.from(currentImgs)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(Schedulers.newThread())
                .map(image -> image.getUrl())
                .flatMap(s -> RequestManager.getInstance().uploadNewsImg("2013211594", s))
                .buffer(mImgs.size() - 1)
                .flatMap(uploadImgResponses -> {
                    for (UploadImgResponse uploadImgResponse : uploadImgResponses) {
                        photosrcs[0] += photosrcs[0] + uploadImgResponse.getData().getPhotosrc() + ",";
                        thumbnail_srcs[0] += thumbnail_srcs[0] + uploadImgResponse.getData().getPhotosrc() + ",";
                    }
                    return RequestManager.getInstance().sendDynamic(5, "天地大同", "8", "122334", thumbnail_srcs[0], photosrcs[0], "2013211594", "160155");
                })
                .subscribe(okResponse -> {
                    if (okResponse.getState() == OkResponse.RESPONSE_OK) {
                        closeLoadingProgress();
                        showUploadSucess();
                    }
                }, throwable -> {
                    closeLoadingProgress();
                    showUploadFail();
                });
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

    private void showUploadFail() {
    }

    private void showUploadSucess() {
    }

    private void showLoadingProgress() {

    }

    private void closeLoadingProgress() {

    }
}
