package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.NineGridlayout;
import com.mredrock.cyxbsmobile.model.community.BBDD;
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.model.community.Stu;
import com.mredrock.cyxbsmobile.model.community.UploadImgResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddNewsActivity extends BaseActivity implements View.OnClickListener {

    private final static String ADD_IMG = "file:///android_asset/add_news.jpg";
    private final static int REQUEST_IMAGE = 0001;
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
    private List<Image> mImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mImgs = new ArrayList<>();
        mImgs.add(new Image(ADD_IMG, Image.ADDIMAG));
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

        mNineGridlayout.setmOnClickDeletecteListener((v, position) -> {
            mImgs.remove(position);
            mNineGridlayout.setImagesData(mImgs);
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_cancel:
                AddNewsActivity.this.finish();
                break;
            case R.id.toolbar_save:
                sendDynamic("标题我该打什么才好？？", mAddNewsEdit.getText().toString(), BBDD.BBDD);
                break;
        }
    }

    private void sendDynamic(String title, String content, int type) {

        if (content.equals("") || content == null) {
            Toast.makeText(AddNewsActivity.this, getString(R.string.noContent), Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<OkResponse> observable;
        List<Image> currentImgs = new ArrayList<>();
        currentImgs.addAll(mImgs);
        currentImgs.remove(0);

        if (currentImgs.size() > 0) {
            observable = uploadWithImg(currentImgs, title, content, type);
        } else {
            observable = uploadWithoutImg(title, content, type);
        }
        observable.subscribe(okResponse -> {
            if (okResponse.getState() == OkResponse.RESPONSE_OK) {
                closeLoadingProgress();
                showUploadSucess();
            }
        }, throwable -> {
            closeLoadingProgress();
            showUploadFail(throwable.toString());
        });


    }

    private Observable<OkResponse> uploadWithImg(List<Image> currentImgs, String title, String content, int type) {
        final String[] photoSrc = {""};
        final String[] thumbnailSrc = {""};
        return Observable.from(currentImgs)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(Schedulers.newThread())
                .map(image -> image.getUrl())
                .flatMap(url -> RequestManager.getInstance().uploadNewsImg(Stu.STU_NUM, url))
                .buffer(currentImgs.size())
                .flatMap(uploadImgResponses -> {
                    for (UploadImgResponse uploadImgResponse : uploadImgResponses) {
                        photoSrc[0] += photoSrc[0] + uploadImgResponse.getData().getPhotosrc().split("/")[6] + ",";
                        thumbnailSrc[0] += thumbnailSrc[0] + uploadImgResponse.getData().getPhotosrc().split("/")[6] + ",";
                    }
                    return RequestManager.getInstance().sendDynamic(type, title, content, thumbnailSrc[0], photoSrc[0]);
                });
    }

    private Observable<OkResponse> uploadWithoutImg(String title, String content, int type) {
        return RequestManager.getInstance()
                .sendDynamic(type, title, content, " ", " ")
                .doOnSubscribe(() -> showLoadingProgress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {


            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                if (mImgs.size() + path.size() > 10) {
                    Toast.makeText(this, "最多只能选9张图", Toast.LENGTH_SHORT).show();
                    return;
                }
                Observable.from(path)
                        .map(s -> new Image(s, Image.NORMALIMAGE))
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

    private void showUploadFail(String reason) {
        DialogUtil.showLoadSucess(this, "提示", "发表动态失败" + reason, "重新发表", "返回", new DialogUtil.DialogListener() {
            @Override
            public void onPositive() {
                closeLoadingProgress();
            }

            @Override
            public void onNegative() {
                closeLoadingProgress();
                AddNewsActivity.this.finish();
            }
        });
    }

    private void showUploadSucess() {
        DialogUtil.showLoadSucess(this, "提示", "发表动态成功", "继续发表", "返回", new DialogUtil.DialogListener() {
            @Override
            public void onPositive() {
                closeLoadingProgress();
            }

            @Override
            public void onNegative() {
                closeLoadingProgress();
                AddNewsActivity.this.finish();
            }
        });
    }

    private void showLoadingProgress() {
        DialogUtil.showLoadingDiaolog(this, "上传中");
    }

    private void closeLoadingProgress() {
        DialogUtil.dismissDialog();
    }
}
