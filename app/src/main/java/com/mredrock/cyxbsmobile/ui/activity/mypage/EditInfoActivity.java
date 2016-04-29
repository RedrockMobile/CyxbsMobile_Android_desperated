package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.community.UploadImgResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.fragment.MyPageFragment;
import com.mredrock.cyxbsmobile.util.DialogUtil;
import com.mredrock.cyxbsmobile.util.ImageLoader;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import okhttp3.MediaType;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditInfoActivity extends BaseActivity
        implements View.OnClickListener {

    public static final String EXTRA_USER = "extra_user";
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final int REQUEST_SELECT_CAMERA = 0x01 + 10;
    public static final int REQUEST_EDIT_NICKNAME = REQUEST_SELECT_PICTURE +
            20;
    public static final int REQUEST_EDIT_INTRODUCE = REQUEST_SELECT_PICTURE +
            30;
    private static final String PATH_CROP_PICTURES =
            Environment.getExternalStorageDirectory() + "/cyxbsmobile/" +
                    "Pictures";

    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.edit_info_avatar_layout) RelativeLayout editInfoAvatarLayout;
    @Bind(R.id.edit_info_nick_layout) RelativeLayout editInfoNickLayout;
    @Bind(R.id.edit_info_introduce_layout) RelativeLayout
            editInfoIntroduceLayout;
    @Bind(R.id.edit_info_avatar) CircleImageView editInfoAvatar;
    @Bind(R.id.edit_info_nick_name) TextView editInfoNickName;
    @Bind(R.id.edit_info_introduce) TextView editInfoIntroduce;

    private User mUser;
    private CharSequence[] dialogItems = { "拍照", "从相册中选择" };
    private Uri mDestinationUri,cameraImageUri;
    private File dir;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        initToolbar();
        editInfoAvatarLayout.setOnClickListener(this);
        editInfoIntroduceLayout.setOnClickListener(this);
        editInfoNickLayout.setOnClickListener(this);

        mUser = getIntent().getParcelableExtra(EXTRA_USER);
        mUser.idNum = "26722X";
        initView();
    }


    private void initView() {
        ImageLoader.getInstance().loadImageWithTargetView(mUser.photo_thumbnail_src,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        editInfoAvatar.setImageBitmap(resource);
                    }
                });
        editInfoNickName.setText(mUser.nickname);
        editInfoIntroduce.setText(mUser.introduction);
        dir = new File(PATH_CROP_PICTURES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        cameraImageUri = Uri.fromFile(new File(dir, System.currentTimeMillis()+".png"));
        mDestinationUri = Uri.fromFile(new File(dir, mUser.stuNum + ".png"));
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info_avatar_layout:
                showSelectDialog();
                break;
            case R.id.edit_info_nick_layout:
                Intent intent = new Intent(this, EditNickNameActivity.class);
                intent.putExtra(EXTRA_USER,mUser);
                startActivityForResult(intent,REQUEST_EDIT_NICKNAME);
                break;
            case R.id.edit_info_introduce_layout:
                Intent intent1 = new Intent(this, EditIntroduceActivity.class);
                intent1.putExtra(EXTRA_USER, mUser);
                startActivityForResult(intent1,REQUEST_EDIT_INTRODUCE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PICTURE:
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startuCropActivity(selectedUri);
                    }else {
                        Toast.makeText(this, "无法识别该图像", Toast.LENGTH_SHORT)
                             .show();
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
                case REQUEST_EDIT_NICKNAME:
                    String nickName = data.getStringExtra(EditNickNameActivity
                            .EXTRA_NICK_NAME);
                    editInfoNickName.setText(nickName);
                    mUser.nickname = nickName;
                    break;
                case REQUEST_EDIT_INTRODUCE:
                    String introduce = data.getStringExtra
                            (EditIntroduceActivity.EXTRA_EDIT_INTRODUCE);
                    editInfoIntroduce.setText(introduce);
                    mUser.introduction = introduce;
                    break;
                case REQUEST_SELECT_CAMERA:
                    startuCropActivity(cameraImageUri);
                    break;
                default:
                    break;
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("修改信息");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_USER,mUser);
                setResult(RESULT_OK,intent);
                EditInfoActivity.this.finish();
            });
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }


    private void showSelectDialog() {
        new MaterialDialog.Builder(this).title("头像修改")
                                        .items(dialogItems)
                                        .itemsCallback(
                                                (dialog, itemView, which, text) -> {
                                                    if (which == 0) {
                                                        getImageFromCamera();
                                                    }else {
                                                        getImageFromAlbum();
                                                    }
                                                })
                                        .show();
    }


    private void startuCropActivity(@NonNull Uri uri) {
        UCrop uCrop = UCrop.of(uri, mDestinationUri);
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionQuality(90);
        options.setToolbarColor(
                ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(
                ContextCompat.getColor(this, R.color.colorPrimaryDark));
        uCrop.withOptions(options);
        uCrop.start(this);
    }


    private void handleCropResult(Intent result) {
        Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            showProgress();
            RequestManager.getInstance()
                          .uploadNewsImg(mUser.stunum, resultUri.getPath())
                          .subscribeOn(Schedulers.io())
                          .flatMap(uploadImgResponse -> {
                              UploadImgResponse.DataBean dataBean
                                      = uploadImgResponse.getData();
                              mUser.photo_thumbnail_src = dataBean
                                      .getThumbnail_src();
                              mUser.photo_src = dataBean.getPhotosrc();
                              return RequestManager.getInstance()
                                                   .setPersonInfo(mUser.stunum,
                                                           mUser.idNum,
                                                           dataBean.getThumbnail_src(),
                                                           dataBean.getPhotosrc());
                          })
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribe(okResponse -> {
                              dismissProgress();
                              Toast.makeText(this, "修改头像成功", Toast.LENGTH_SHORT)
                                   .show();
                              editInfoAvatar.setImageURI(resultUri);
                          }, throwable -> {
                              dismissProgress();
                              Toast.makeText(this,
                                      "修改头像失败：" + throwable.getMessage(),
                                      Toast.LENGTH_SHORT).show();
                              Log.e("------------------>",
                                      throwable.toString());
                          });
        }
    }


    private void handleCropError(Intent result) {
        Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_SHORT)
                 .show();
        }
        else {
            Toast.makeText(this, "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }

    private void getImageFromAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_SELECT_PICTURE);
    }

    private void getImageFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cameraImageUri);
        startActivityForResult(intent,REQUEST_SELECT_CAMERA);
    }

    private void showProgress() {
        DialogUtil.showLoadingDiaolog(this, "上传中");
    }


    private void dismissProgress() {
        DialogUtil.dismissDialog();
    }
}
