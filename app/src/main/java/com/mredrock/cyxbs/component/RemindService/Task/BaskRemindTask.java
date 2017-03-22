package com.mredrock.cyxbs.component.RemindService.Task;

import android.content.Context;

import com.mredrock.cyxbs.component.RemindService.Reminder;

import java.util.ArrayList;

/**
 * Created by simonla on 2017/3/22.
 * 下午12:04
 */

public abstract class BaskRemindTask {

   public Context mContext;

   BaskRemindTask(Context context) {
      mContext=context;
   }

   /**
    * 提醒有开闭两种状态，该方法会告诉Manager是否该执行任务
    * @return 是否开启该类型的提醒
    */
   public abstract boolean isTurnOn();

   /**
    * 所有任务都是异步的
    * @param callback 获取到要执行的任务后给callback接口
    */
   public abstract void task(Callback callback);

   public interface Callback{
      void done(ArrayList<Reminder> reminders);
   }
}
