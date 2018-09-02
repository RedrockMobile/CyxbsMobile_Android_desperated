package com.mredrock.cyxbs.util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.ElectricCharge;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.explore.electric.DormitorySettingActivity;
import com.mredrock.cyxbs.ui.activity.explore.electric.ElectricChargeActivity;
import com.mredrock.cyxbs.ui.activity.explore.electric.ElectricRemindActivity;

/**
 * Created by ：AceMurder
 * Created on ：2017/4/23
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class ElectricRemindUtil {
    public static final String SP_KEY_ELECTRIC_REMIND_TIME = "electric_remind_check_time";
    public static final String CHANNEL_ID = "electric_remind";
    public static final String CHANNEL_NAME = "电费提醒";

    private static final String TAG = "ElectricRemindUtil";

    public static void check(Context context) {

        long time = (long) SPUtils.get(BaseAPP.getContext(), SP_KEY_ELECTRIC_REMIND_TIME, System.currentTimeMillis() / 2);
        SPUtils.set(BaseAPP.getContext(), SP_KEY_ELECTRIC_REMIND_TIME, System.currentTimeMillis());
        if (System.currentTimeMillis() - time < 60 * 60 * 1000 * 6) {
            Log.i(TAG, "check: " + (System.currentTimeMillis() - time));
            return;
        }

        int buildingPosition = (int) SPUtils.get(BaseAPP.getContext(), DormitorySettingActivity.BUILDING_KEY, -1);
        if (buildingPosition < 0)
            return;
        String dormitoryNum = (String) SPUtils.get(BaseAPP.getContext(), DormitorySettingActivity.DORMITORY_KEY, "");
        float money = (float) SPUtils.get(BaseAPP.getContext(), ElectricRemindActivity.ELECTRIC_REMIND_MONEY, -1.0f);
        if (money == -1)
            return;
        String building = BaseAPP.getContext().getResources().getStringArray(R.array.dormitory_buildings_api)[buildingPosition];
        RequestManager.INSTANCE.queryElectricCharge(new SimpleObserver<>(context, new SubscriberListener<ElectricCharge>() {
            @Override
            public void onNext(ElectricCharge electricCharge) {
                super.onNext(electricCharge);
                if (electricCharge.getElectricCost().size() == 0)
                    return;
                String data = electricCharge.getElectricCost().get(0) + "." + electricCharge.getElectricCost().get(1);
                if (Float.parseFloat(data) > money) {
                    Log.i(TAG, "remind money" + money + "  const：" + data);
                    Intent nextIntent = new Intent(context, ElectricChargeActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel(context, CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("电费超额提醒")
                            .setContentText("小邮提醒您，您的电费已超过设置的限额，注意节约用电哦");
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(123, builder.build());

                }
            }
        }), building, dormitoryNum);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context, String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
}
