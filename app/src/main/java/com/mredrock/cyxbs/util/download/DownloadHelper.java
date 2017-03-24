package com.mredrock.cyxbs.util.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.Utils;
import com.mredrock.cyxbs.util.download.callback.OnDownloadListener;
import com.mredrock.cyxbs.util.download.progress.ProgressHelper;
import com.mredrock.cyxbs.util.download.progress.UIProgressListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Stormouble on 15/12/9.
 */
public class DownloadHelper {
    public static final String[][] MIME_TABLE = {
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".pdf", "application/pdf"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
    };
    /**
     * 实际请求的次数
     */
    private static int mReqCount;

    /**
     * 需要请求的次数
     */
    private static int mNeedReqCount;
    private Context mContext;
    private boolean mIsSuccess;
    private boolean mIsJWZXNews;
    /**
     * 需要下载的链接
     */
    private List<String> mUrls;
    /**
     * 需要下载的文件名
     */
    private List<String> mFileNames;
    /**
     * 下载成功的文件
     */
    private List<File> mDownloadFiles;
    private MaterialDialog mProgressDialog;
    private OnDownloadListener mDownloadListener;
    private WeakHandler mWeakHandler;

    public DownloadHelper(Context context, boolean isJWZX) {
        mContext = context;
        mIsJWZXNews = isJWZX;
        mIsSuccess = true;
        mWeakHandler = new WeakHandler();
        mUrls = new ArrayList<>();
        mFileNames = new ArrayList<>();
        mDownloadFiles = new ArrayList<>();
        mProgressDialog = new MaterialDialog.Builder(mContext)
                .title(R.string.news_load)
                .content(R.string.loading)
                .progress(true, 0)
                .build();
    }

    /**
     * 初始化准备
     *
     * @param nameList 待下载文件名
     * @param urlList  待下载文件的链接
     * @param listener 下载回调
     */
    public void prepare(List<String> nameList, List<String> urlList, OnDownloadListener listener) {
        if (mContext != null && listener != null) {
            initialize(listener);

            String[] items = new String[nameList.size()];
            //显示下载dialog
            new MaterialDialog.Builder(mContext)
                    .title(mContext.getResources().getString(R.string.news_load))
                    .items(nameList.toArray(items))
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog materialDialog, Integer[] integers, CharSequence[] charSequences) {
                            if (integers.length != 0) {
                                for (Integer position : integers) {
                                    mUrls.add(urlList.get(position));
                                    mFileNames.add(nameList.get(position));

                                }
                                mNeedReqCount = mUrls.size();
                                if (mDownloadListener != null) {
                                    mDownloadListener.startDownload();
                                    tryDownload();
                                }
                            }
                            return true;
                        }
                    })
                    .positiveText(mContext.getResources().getString(R.string.load_agree))
                    .negativeText(mContext.getResources().getString(R.string.load_disagree))
                    .show();
        }
    }

    /**
     * 初始化
     */
    private void initialize(OnDownloadListener listener) {
        mDownloadListener = listener;
        mReqCount = 0;
        mUrls.clear();
        mFileNames.clear();
        mDownloadFiles.clear();
    }

    /**
     * 下载
     */
    public void tryDownload() {
        if (mIsJWZXNews) {
            downloadCorrectUrl(mDownloadListener);
        } else {
            download();
        }
    }

    /**
     * 教务在线新闻请求获取正确链接
     */
    private void downloadCorrectUrl(OnDownloadListener listener) {
        final int[] reqSuccessCount = new int[1];
        reqSuccessCount[0] = 0;
        showDialog(mProgressDialog);
        Log.d("mUrls", mUrls.toString());
        mWeakHandler.post(this::download);
    }

    private void download() {
        //进度回调
        final UIProgressListener progressListener = new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
            }

            @Override
            public void onUIStart(long currentBytes, long contentLength, boolean done) {
                super.onUIStart(currentBytes, contentLength, done);
            }

            @Override
            public void onUIFinish(long currentBytes, long contentLength, boolean done) {
                super.onUIFinish(currentBytes, contentLength, done);
                if (mReqCount == mNeedReqCount) {
                    dismissDialog(mProgressDialog);
                    if (mIsSuccess) {
                        mDownloadListener.downloadSuccess();

                        String path = Utils.getExternalStoragePath() + "/Download";
                        Utils.toast(mContext, String.format(mContext.getResources().getString(R.string.load_file_path), path));
                    } else {
                        mDownloadListener.downloadFailed("request failed");
                    }
                }
            }
        };

        if (Utils.getExternalStoragePath() != null) {
            if (!mIsJWZXNews) {
                showDialog(mProgressDialog);
            }

            showDialog(mProgressDialog);
            for (int i = 0; i < mUrls.size(); i++) {
                String path = Utils.getExternalStoragePath() + "/Download/" + mFileNames.get(i);
                if (!mIsJWZXNews) {
                    String url = mUrls.get(i).replaceAll("localhost", "hongyan.cqupt.edu.cn");
                    tryLoad(url, path, progressListener);
                } else {
                    tryLoad(mUrls.get(i), path, progressListener);
                }
            }
        }
    }

    public void showDialog(MaterialDialog dialog) {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismissDialog(MaterialDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 发起请求, 进行下载
     *
     * @param url              链接
     * @param path             存储路径
     * @param progressListener 进度回调接口
     */
    private void tryLoad(String url, String path, UIProgressListener progressListener) {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        ProgressHelper.addProgressResponseListener(client, progressListener).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mIsSuccess = false;
                mReqCount++;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    InputStream is = null;
                    FileOutputStream fos = null;
                    File file = null;
                    byte[] bytes = new byte[1024];
                    int length = 0;
                    try {
                        file = new File(path);
                        is = response.body().byteStream();
                        fos = new FileOutputStream(file);
                        while ((length = is.read(bytes)) != -1) {
                            fos.write(bytes, 0, length);
                        }
                        fos.flush();

                        mReqCount++;
                        mDownloadFiles.add(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            fos.close();
                        }
                    }
                } else {
                    mIsSuccess = false;
                    mReqCount++;
                }
            }
        });
    }

    /**
     * 打开下载文件
     */
    public void tryOpenFile() {
        if (mIsSuccess) {
            String[] items = new String[mDownloadFiles.size()];
            for (int i = 0; i < mDownloadFiles.size(); i++) {
                items[i] = mDownloadFiles.get(i).getName();
            }
            new MaterialDialog.Builder(mContext)
                    .title(R.string.load_open_file)
                    .items(items)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which != -1) {
                                File file = mDownloadFiles.get(which);
                                if (file != null && file.exists()) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    String type = getMIMEType(file);
                                    intent.setDataAndType(Uri.fromFile(file), type);
                                    try {
                                        mContext.startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            return true;
                        }
                    })
                    .positiveText(R.string.load_agree)
                    .negativeText(mContext.getResources().getString(R.string.load_disagree))
                    .show();

        } else {
            throw new IllegalStateException("download failed");
        }
    }

    /**
     * 获取文件类型
     */
    public String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();

        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end.equals("")) return type;

        for (int i = 0; i < MIME_TABLE.length; i++) {
            if (end.equals(MIME_TABLE[i][0]))
                type = MIME_TABLE[i][1];
        }
        return type;
    }
}

