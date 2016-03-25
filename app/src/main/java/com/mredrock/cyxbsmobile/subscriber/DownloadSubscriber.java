package com.mredrock.cyxbsmobile.subscriber;

import android.content.Context;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.util.FileUtils;

import java.io.File;

import okhttp3.ResponseBody;

/**
 *  一个非常简单的下载信息订阅者，用于将下载的信息保存到文件，Toast显示保存信息
 */
public class DownloadSubscriber extends SimpleSubscriber<ResponseBody> {

    private File fileDir;
    private String fileName;

    /**
     * @param context  上下文
     * @param listener 为了自己重写onNext, onError等回调的监听器
     * @param fileDir  文件要保存的目录
     * @param fileName 文件名
     */
    public DownloadSubscriber(Context context, SubscriberListener<ResponseBody> listener, File fileDir, String fileName) {
        this(context, false, listener, fileDir, fileName);
    }

    public DownloadSubscriber(Context context, boolean shouldShowProgressDialog, SubscriberListener<ResponseBody> listener, File fileDir, String fileName) {
        this(context, shouldShowProgressDialog, false, listener, fileDir, fileName);
    }

    public DownloadSubscriber(Context context, boolean shouldShowProgressDialog, boolean isProgressDialogCancelable, SubscriberListener<ResponseBody> listener, File fileDir, String fileName) {
        super(context, shouldShowProgressDialog, isProgressDialogCancelable, listener);
        this.fileDir = fileDir;
        this.fileName = fileName;
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        super.onNext(responseBody);
        String info = FileUtils.saveFile(responseBody, fileDir, fileName);
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }
}
