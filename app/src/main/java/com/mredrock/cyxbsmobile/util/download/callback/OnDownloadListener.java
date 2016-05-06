package com.mredrock.cyxbsmobile.util.download.callback;

/**
 * Created by Stormouble on 15/12/10.
 */
public interface OnDownloadListener {

    void startDownload();

    void downloadSuccess();

    void downloadFailed(String message);

}
