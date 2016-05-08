package com.mredrock.cyxbs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Stormouble on 16/5/8.
 */
public class MapHelper {
    public static final int SAVE_PICTURE_SUCCESS = 1;
    public static final int SAVE_PICTURE_FAILED = 2;
    public static final int READ_PICTURE = 3;
    public static final int LOAD_PICTURE = 4;

    private static final int DEFAULT_TIMEOUT = 15;

    private static final String MAP_PICTURE_NAME = "map.png";

    private final String mCachePath;
    private List<Future> mTaskList;

    private final Context mContext;
    private final Handler mMainHandler;
    private final ExecutorService mService;

    public MapHelper(Context context, Handler mainHandler) {
        mContext = context;
        mMainHandler = mainHandler;
        mCachePath = Utils.getDiskCacheDir(context).getPath() + File.separator + MAP_PICTURE_NAME;
        mService = Executors.newCachedThreadPool(new MapThreadFactory());
        mTaskList = new ArrayList<>();
    }

    public void saveMapPicture(Bitmap bitmap) {
        Future future = mService.submit((Runnable) () -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(mCachePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                mMainHandler.obtainMessage(SAVE_PICTURE_SUCCESS).sendToTarget();
            } catch (Exception e) {
                mMainHandler.obtainMessage(SAVE_PICTURE_FAILED).sendToTarget();
                e.printStackTrace();
            }
        });
        mTaskList.add(future);
    }

    public void readMapPictureFromCache() {
        Future future = mService.submit((Runnable) () -> {
            Bitmap result = null;
            try {
                FileInputStream fis = new FileInputStream(mCachePath);
                result = BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            mMainHandler.obtainMessage(READ_PICTURE, result).sendToTarget();
        });
        mTaskList.add(future);
    }

    public void loadPicture(String url) {
        Future future = mService.submit((Runnable) () -> {
            OkHttpClient client = configurationOkHttpClient(new OkHttpClient.Builder());
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Bitmap result = null;
            try {
                Response response = client.newCall(request).execute();
                InputStream in = response.body().byteStream();
                result = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result != null) {
                saveMapPicture(result);
            }
            mMainHandler.obtainMessage(LOAD_PICTURE, result).sendToTarget();
        });
        mTaskList.add(future);
    }

    public void cancel() {
        for (Future task : mTaskList) {
            task.cancel(false);
        }
    }

    public void cleanUp() {
        cancel();
        mTaskList.clear();
        mTaskList = null;
    }

    private OkHttpClient configurationOkHttpClient(OkHttpClient.Builder builder) {
        File internalCacheDir = mContext.getCacheDir();

        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.cache(new Cache(new File(internalCacheDir.getPath()),
                Runtime.getRuntime().maxMemory() / 1024 / 8));
        return builder.build();
    }

    public static class MapThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new MapThread(r);
        }
    }

    public static class MapThread extends Thread {

        public MapThread(Runnable runnable) {
            super(runnable);
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            super.run();
        }
    }
}
