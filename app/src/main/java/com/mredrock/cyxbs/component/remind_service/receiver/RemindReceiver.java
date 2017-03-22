package com.mredrock.cyxbs.component.remind_service.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.MainActivity;

import static com.mredrock.cyxbs.component.remind_service.service.NotificationService.EXTRA_NOTIFY_SUBTITLE;
import static com.mredrock.cyxbs.component.remind_service.service.NotificationService.EXTRA_NOTIFY_TITLE;

/**
 * Created by simonla on 2016/10/10.
 * 下午11:06
 */

public class RemindReceiver extends BroadcastReceiver {

    public static final String TAG = RemindReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        remind(context, intent);
    }

    private void remind(Context context, Intent intent) {
        Intent nextIntent = new Intent(context, MainActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(nextIntent);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(intent.getStringExtra(EXTRA_NOTIFY_TITLE))
                .setContentText(intent.getStringExtra(EXTRA_NOTIFY_SUBTITLE));
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
