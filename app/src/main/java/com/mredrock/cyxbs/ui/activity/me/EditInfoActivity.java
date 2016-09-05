package com.mredrock.cyxbs.ui.activity.me;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.UploadImgResponse;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.DialogUtil;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.permission.AfterPermissionGranted;
import com.mredrock.cyxbs.util.permission.EasyPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EditInfoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String PATH_CROP_PICTURES = Environment.getExternalStorageDirectory() + "/cyxbsmobile/" + "Pictures";

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edit_info_avatar_layout)
    RelativeLayout editInfoAvatarLayout;
    @Bind(R.id.edit_info_nick_layout)
    RelativeLayout editInfoNickLayout;
    @Bind(R.id.edit_info_introduce_layout)
    RelativeLayout editInfoIntroduceLayout;
    @Bind(R.id.edit_info_avatar)
    ImageView editInfoAvatar;
    @Bind(R.id.edit_info_nick_name)
    TextView editInfoNickName;
    @Bind(R.id.edit_info_introduce)
    TextView editInfoIntroduce;
    @Bind(R.id.edit_info_qq)
    TextView editInfoQq;
    @Bind(R.id.edit_info_qq_layout)
    RelativeLayout editInfoQqLayout;
    @Bind(R.id.edit_info_phone)
    TextView editInfoPhone;
    @Bind(R.id.edit_info_phone_layout)
    RelativeLayout editInfoPhoneLayout;

    private User mUser;
    private CharSequence[] dialogItems = {"拍照", "从相册中选择"};
    private Uri mDestinationUri, cameraImageUri;
    private File dir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        initToolbar();
        mUser = APP.getUser(this);
        initView();
    }


    private void initView() {
        ImageLoader.getInstance().loadAvatar(mUser.photo_thumbnail_src, editInfoAvatar);
        editInfoNickName.setText(mUser.nickname);
        editInfoIntroduce.setText(mUser.introduction);
        editInfoQq.setText(mUser.qq);
        editInfoPhone.setText(mUser.phone);
        dir = new File(PATH_CROP_PICTURES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        cameraImageUri = Uri.fromFile(
                new File(dir, System.currentTimeMillis() + ".png"));
        mDestinationUri = Uri.fromFile(new File(dir, mUser.stuNum + ".png"));
    }

    @OnClick(R.id.edit_info_avatar_layout)
    void clickToDialog() {
        showSelectDialog();
    }

    @OnClick(R.id.edit_info_nick_layout)
    void clickToNickName() {
        Intent intent = new Intent(this, EditNickNameActivity.class);
        intent.putExtra(Const.Extras.EDIT_USER, mUser);
        startActivityForResult(intent, Const.Requests.EDIT_NICKNAME);
    }

    @OnClick(R.id.edit_info_introduce_layout)
    void clickToIntroduce() {
        Intent intent = new Intent(this, EditIntroduceActivity.class);
        intent.putExtra(Const.Extras.EDIT_USER, mUser);
        startActivityForResult(intent, Const.Requests.EDIT_INTRODUCE);
    }

    @OnClick(R.id.edit_info_qq_layout)
    void clickToQQ() {
        Intent intent = new Intent(this, EditQQActivity.class);
        intent.putExtra(Const.Extras.EDIT_USER, mUser);
        startActivityForResult(intent, Const.Requests.EDIT_QQ);
    }

    @OnClick(R.id.edit_info_phone_layout)
    void clickToPhone() {
        Intent intent = new Intent(this, EditPhoneActivity.class);
        intent.putExtra(Const.Extras.EDIT_USER, mUser);
        startActivityForResult(intent, Const.Requests.EDIT_PHONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.Requests.SELECT_PICTURE:
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startuCropActivity(selectedUri);
                    } else {
                        Toast.makeText(this, "无法识别该图像", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
                case Const.Requests.EDIT_NICKNAME:
                    String nickName = data.getStringExtra(Const.Extras.EDIT_NICK_NAME);
                    editInfoNickName.setText(nickName);
                    mUser.nickname = nickName;
                    break;
                case Const.Requests.EDIT_INTRODUCE:
                    String introduce = data.getStringExtra(
                            EditIntroduceActivity.EXTRA_EDIT_INTRODUCE);
                    editInfoIntroduce.setText(introduce);
                    mUser.introduction = introduce;
                    break;
                case Const.Requests.EDIT_QQ:
                    String qq = data.getStringExtra(Const.Extras.EDIT_QQ);
                    editInfoQq.setText(qq);
                    mUser.qq = qq;
                    break;
                case Const.Requests.EDIT_PHONE:
                    String phone = data.getStringExtra(Const.Extras.EDIT_PHONE);
                    editInfoPhone.setText(phone);
                    mUser.phone = phone;
                    break;
                case Const.Requests.SELECT_CAMERA:
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
                intent.putExtra(Const.Extras.EDIT_USER, mUser);
                setResult(RESULT_OK, intent);
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
        new MaterialDialog.Builder(this)
                .items(dialogItems)
                .itemsCallback(
                        (dialog, itemView, which, text) -> {
                            if (which == 0) {
                                getImageFromCamera();
                            } else {
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
                    .uploadNewsImg(mUser.stuNum, resultUri.getPath())
                    .subscribeOn(Schedulers.io())
                    .flatMap((Func1<UploadImgResponse.Response, Observable<?>>) response -> {
                        mUser.photo_thumbnail_src = response.thumbnailSrc;
                        mUser.photo_src = response.photoSrc;
                        return RequestManager.getInstance()
                                .setPersonInfo(mUser.stuNum,
                                        mUser.idNum,
                                        response.thumbnailSrc,
                                        response.photoSrc);
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
                    });
        }
    }

    private void handleCropError(Intent result) {
        Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }


    @AfterPermissionGranted(1)
    private void getImageFromAlbum() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, permissions)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    "image/*");
            startActivityForResult(intent, Const.Requests.SELECT_PICTURE);
        } else {
            EasyPermissions.requestPermissions(this, "读取图片需要访问您的存储空间哦~",
                    1, permissions);
        }

    }


    @AfterPermissionGranted(2)
    private void getImageFromCamera() {
        String[] permissions = {Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, permissions)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            startActivityForResult(intent, Const.Requests.SELECT_CAMERA);
        } else {
            EasyPermissions.requestPermissions(this, "拍照需要访问你的相机哦~",
                    2, permissions);
        }

    }


    private void showProgress() {
        DialogUtil.showLoadingDiaolog(this, "上传中");
    }


    private void dismissProgress() {
        DialogUtil.dismissDialog();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
}
