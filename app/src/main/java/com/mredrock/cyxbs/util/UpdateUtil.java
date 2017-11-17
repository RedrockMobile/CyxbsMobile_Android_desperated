package com.mredrock.cyxbs.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.config.Config;
import com.mredrock.cyxbs.model.UpdateInfo;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.service.UpdateService;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by cc on 16/5/8.
 */
public class UpdateUtil {

    public static void checkUpdate(Context context, boolean shouldReturnResult,RxPermissions permissions) {
        RequestManager.getInstance()
                      .checkUpdate(new SimpleObserver<>(context, shouldReturnResult, false, new SubscriberListener<UpdateInfo>() {
                                  @Override
                                  public void onNext(UpdateInfo updateInfo) {
                                      if (updateInfo != null) {
                                          new MaterialDialog.Builder(context)
                                                  .title("更新")
                                                  .title("有新版本更新")
                                                  .content("最新版本:" + updateInfo.versionName + "\n" + updateInfo.updateContent + "\n点击点击，现在就更新一发吧~")
                                                  .positiveText("更新")
                                                  .negativeText("下次吧")
                                                  .onNegative((dialog, which) -> dialog.dismiss())
                                                  .onPositive(((dialog, which) -> permissions
                                                          .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                                  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                          .subscribe(granted -> {
                                                              if (granted) {
                                                                  startDownload(updateInfo.apkURL, context);
                                                              } else {
                                                                  Utils.toast(context, "没有赋予权限就不能更新哦");
                                                              }
                                                          })))
                                                  .cancelable(false)
                                                  .show();

                                      } else if (shouldReturnResult) {
                                          Utils.toast(context.getApplicationContext(), "已经是最新版了");
                                      }
                                  }
                              }),
                              Utils.getAppVersionCode(context)
                      );
    }

    private static void startDownload(String urlStr, Context context) {
        Intent updateIntent = new Intent(context, UpdateService.class);
        updateIntent.putExtra("url", urlStr);
        updateIntent.putExtra("path", Config.updateFilePath);
        updateIntent.putExtra("name", Config.updateFilename);
        context.startService(updateIntent);
    }
}
