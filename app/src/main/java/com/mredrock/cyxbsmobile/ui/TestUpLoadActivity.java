package com.mredrock.cyxbsmobile.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.network.service.UpDownloadService;
import com.mredrock.cyxbsmobile.subscriber.DownloadSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Deprecated
public class TestUploadActivity extends AppCompatActivity {

    @Bind(R.id.result_image)
    ImageView resultView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TestUploadActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.test_btn_download)
    public void onDownloadClick() {
        RequestManager.getInstance().download(UpDownloadService.TEST_PIC_URL,
                new DownloadSubscriber(this, null, Environment.getExternalStorageDirectory(), "PIC.jpg"));
    }

    @OnClick(R.id.test_btn_upload)
    public void onUploadClick() {
        resultView.setImageDrawable(null);
        Crop.pickImage(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_upload);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            /* 测试上传 */
            RequestManager.getInstance().uploadTest(new SimpleSubscriber<>(this, true, new SubscriberListener<String>() {
                @Override
                public void onNext(String info) {
                    Toast.makeText(TestUploadActivity.this, info, Toast.LENGTH_SHORT).show();
                }
            }), Crop.getOutput(result));

            resultView.setImageURI(Crop.getOutput(result));

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
