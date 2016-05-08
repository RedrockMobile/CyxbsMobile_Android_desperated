package com.mredrock.cyxbs.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Process;

import com.mredrock.cyxbs.network.RequestManager;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
    public static final int ON_PROGRESS = 5;

    private static final int DEFAULT_CONNETCTION_TIMEOUT = 15;

    private static final String MAP_PICTURE_NAME = "map.png";

    private final String cachePath;

    private final Handler mainHandler;
    private final ExecutorService service;

    public MapHelper(Handler mainHandler, File cacheDir) {
        this.mainHandler = mainHandler;
        this.cachePath = cacheDir + File.separator + MAP_PICTURE_NAME;

        this.service = Executors.newCachedThreadPool();
    }

    public void saveMapPicture(Bitmap bitmap) {
        new WorkerThread(() -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(cachePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                mainHandler.obtainMessage(SAVE_PICTURE_SUCCESS).sendToTarget();
            } catch (Exception e) {
                mainHandler.obtainMessage(SAVE_PICTURE_FAILED).sendToTarget();
                e.printStackTrace();
            }

        }).start();
    }

    public void readMapPictureFromCache() {
        new WorkerThread(() -> {
            Bitmap result = null;
            try {
                FileInputStream fis = new FileInputStream(cachePath);
                result = BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            mainHandler.obtainMessage(READ_PICTURE, result).sendToTarget();
        }).start();
    }

    public void loadPicture(String url) {
        new WorkerThread(() -> {
            Request request = new Request.Builder().url(url).get().build();
            Bitmap result = null;
            try {
                Response mapResponse = RequestManager.getInstance().getOkHttpClient().newCall(request).execute();
                InputStream in = mapResponse.body().byteStream();
                result = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result != null) {
                saveMapPicture(result);
                mainHandler.obtainMessage(LOAD_PICTURE, result).sendToTarget();
            } else {
                Logger.d("Load picture failed");
            }
        }).start();
    }

    public void cancel() {

    }

    private OkHttpClient configurationOkttpClient(OkHttpClient.Builder builder) {
        return null;
    }

//    public static class MapThreadFactory extends ThreadFactory {
//
//        @Override
//        public Thread newThread(Runnable r) {
//
//            return null;
//        }
//    }

    public static class WorkerThread extends Thread {

        public WorkerThread(Runnable runnable) {
            super(runnable);
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            super.run();
        }
    }
}
