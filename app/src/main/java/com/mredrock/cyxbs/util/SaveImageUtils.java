package com.mredrock.cyxbs.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.config.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wusui on 16/11/13.
 */

public class SaveImageUtils {
    private static final String TAG = LogUtils.makeLogTag(SaveImageUtils.class);

    public static void imageSave(final Bitmap bitmap, final String url) {
        Observable.create((ObservableOnSubscribe<Bitmap>) e -> {
            boolean isDirectoriesExist = true;
            boolean isImageFileExist = true;

            File imageFile = new File(Environment.getExternalStorageDirectory() + Config.DIR_PHOTO);
            if (!imageFile.exists()) {
                isDirectoriesExist = imageFile.mkdirs();
            }

            if (isDirectoriesExist) {
                FileOutputStream outputStream;
                try {
                    String name = Environment.getExternalStorageDirectory() + Config.DIR_PHOTO + File.separator + Utils.md5Hex(url) + ".jpg";
                    File file = new File(name);
                    if (!file.exists()) {
                        try {
                            isImageFileExist = file.createNewFile();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }

                    if (isImageFileExist) {
                        outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                } catch (IOException ioException) {
                    e.onError(ioException);
                }
            }
            e.onNext(bitmap);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        String name = Environment.getExternalStorageDirectory() + Config.DIR_PHOTO + "/" + Utils.md5Hex(url) + ".jpg";
                        Toast.makeText(BaseAPP.getContext(), "图片成功保存至" + name + "目录", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onNext: " + Environment.getExternalStorageDirectory().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }
}
