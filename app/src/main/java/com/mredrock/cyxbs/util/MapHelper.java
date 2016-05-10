package com.mredrock.cyxbs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.util.cache.DiskCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Stormouble on 16/5/8.
 */
public class MapHelper {
    public static final int LOAD_OVERLAY_SUCCESS = 1;
    public static final int LOAD_OVERLAY_FAILED = 2;

    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    private static final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    private static final int MAX_DISK_CACHE_BYTES = 1024 * 1024 * 10; // 10MB
    private static final String TAG = LogUtils.makeLogTag(MapHelper.class);
    private static final String DEFAULT_MAP_KEY = Utils.md5Hex(Const.END_POINT_REDROCK);

    private final Context mContext;
    private final DiskCache mDiskCache;
    private final Handler mMainHandler;

    public MapHelper(Context context, Handler mainHandler) {
        mContext = context;
        mMainHandler = mainHandler;
        mDiskCache =
                new DiskCache(Utils.getDiskCacheDir(context, "bitmap"), MAX_DISK_CACHE_BYTES);
    }


    public Bitmap getCachedOverlayImage() {
        InputStream inputStream = mDiskCache.get(DEFAULT_MAP_KEY);
        if (inputStream != null) {
            return BitmapFactory.decodeStream(inputStream);
        } else {
            return null;
        }
    }

    public void loadOverlayImage(String url, boolean shouldCache) {
        new MapOverlayAsyncTask(shouldCache).execute(url);
    }

    private OkHttpClient configurationOkHttpClient(OkHttpClient.Builder builder) {
        File cacheDir = new File(mContext.getCacheDir(), "okhttp");

        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.cache(new Cache(cacheDir, MAX_DISK_CACHE_BYTES));
        return builder.build();
    }

    public class MapOverlayAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private boolean shouldCache;

        public MapOverlayAsyncTask(boolean shouldCache) {
            this.shouldCache = shouldCache;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            OkHttpClient client = configurationOkHttpClient(new OkHttpClient.Builder());
            Request request = new Request.Builder()
                    .url(params[0])
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response != null && response.body() != null) {
                    return BitmapFactory.decodeStream(response.body().byteStream());
                }
            } catch (IOException ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                if (shouldCache) {
                    mDiskCache.put(DEFAULT_MAP_KEY, BitmapUtil.getBitmapOutStream(bitmap));
                }
                mMainHandler.obtainMessage(LOAD_OVERLAY_SUCCESS, bitmap).sendToTarget();
            } else {
                mMainHandler.obtainMessage(LOAD_OVERLAY_FAILED).sendToTarget();
            }
        }
    }
}
