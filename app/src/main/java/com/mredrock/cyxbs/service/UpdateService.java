package com.mredrock.cyxbs.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateService extends Service {

    private NotificationManager        notificationManager;
    private NotificationCompat.Builder builder;
    private int notification_id = 1003;
    private DownloadAsyncTask task;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() == null) {
            if (task != null) {
                task.stop();
                task.cancel(true);
                task = null;
                Utils.toast(UpdateService.this, "取消更新");
            }
            return START_NOT_STICKY;
        }
        String url = intent.getExtras().getString("url");
        String filepath = intent.getExtras().getString("path");
        String filename = intent.getExtras().getString("name");

        if (url != null && Utils.isNetWorkAvilable(this)) {
            createNotification();
            task = null;
            task = new DownloadAsyncTask();
            task.execute(url, filepath, filename);
            Utils.toast(UpdateService.this, "新版掌上重邮开始下载了...");
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        task.cancel(true);
        task = null;
        super.onDestroy();
    }

    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                                                      .setContentTitle("掌上重邮更新中...")
                                                      .setContentText("0%");
        builder.setProgress(100, 0, false);

        Intent stopIntent = new Intent(this, UpdateService.class);
        builder.setDeleteIntent(PendingIntent.getService(this, 0, stopIntent, 0));
        builder.setAutoCancel(true);
        builder.setTicker("新版掌上重邮开始下载了...");
    }

    private void updateNotification(int progress) {
        builder.setProgress(100, progress, false);
        builder.setContentText(progress + "%");
        notificationManager.notify(notification_id, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {
        private String  path;
        private String  name;
        private boolean cancelUpdate;

        private void stop() {
            cancelUpdate = true;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            path = params[1];
            name = params[2];
            boolean finish = false;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                if (conn.getResponseCode() == 200) {
                    File f = new File(params[1]);
                    if (!f.isDirectory()) {
                        f.mkdirs();
                    }
                    InputStream is = conn.getInputStream();
                    int length = conn.getContentLength();
                    File file = new File(path + name);
                    if (file.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        file.delete();  // I think maybe the existent file cause the update failure
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    int progress = 0;
                    int progress_pre = 0;
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        if (progress != progress_pre) {
                            publishProgress(progress);
                            progress_pre = progress;
                        }

                        if (numread <= 0) {
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);
                    fos.flush();
                    fos.close();
                    is.close();
                    finish = true;
                }
            } catch (Exception e) {
                finish = false;
            }
            return finish;
        }

        @Override
        protected void onPostExecute(Boolean finish) {
            notificationManager.cancel(notification_id);
            if (finish && !cancelUpdate) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(
                            FileProvider.getUriForFile(getApplicationContext(), "com.mredrock.cyxbs.APKFileProvider", new File(path + name)),
                            "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(
                            Uri.fromFile(new File(path + name)),
                            "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            } else {
                Utils.toast(UpdateService.this, "更新失败");
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            updateNotification(values[0]);
        }

    }
}
