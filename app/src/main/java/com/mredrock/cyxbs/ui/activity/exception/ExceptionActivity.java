package com.mredrock.cyxbs.ui.activity.exception;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.mredrock.cyxbs.BuildConfig;

/**
 * 显示异常 Activity
 * <p><b>仅用于 DEBUG 版本</b>，捕获导致崩溃的异常并显示出来</p>
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ExceptionActivity extends AppCompatActivity {

    private static Thread.UncaughtExceptionHandler originHandler;

    public static final String EXTRA_THREAD_NAME = "thread_name";
    public static final String EXTRA_EXCEPTION = "exception";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(originHandler);
        final String threadName = getIntent().getStringExtra(EXTRA_THREAD_NAME);
        final Throwable t = (Throwable) getIntent().getSerializableExtra(EXTRA_EXCEPTION);
        showExceptionDialog(this, threadName, t);
    }

    public static void start(Context context, Thread t, Throwable e) {
        Intent starter = new Intent(context, ExceptionActivity.class);
        starter.putExtra(EXTRA_THREAD_NAME, t.getName());
        starter.putExtra(EXTRA_EXCEPTION, e);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    private static String generateThrowableStackString(Throwable t) {
        StringBuilder builder = new StringBuilder();
        while (t != null) {
            builder.append(t.getClass().getName()).append('\n');
            builder.append('\t').append("at");
            for (StackTraceElement i: t.getStackTrace()) {
                builder.append('\n').append(i.toString());
            }
            t = t.getCause();
            if (t != null) {
                builder.append('\n').append('\n').append("Cause by: ");
            }
        }
        return builder.toString();
    }

    /**
     * 显示一个带有异常信息的 Dialog
     * @param context
     * @param t
     */
    public static void showExceptionDialog(Context context, Throwable t) {
        showExceptionDialog(context, "unknown-thread", t);
    }

    /**
     * 显示一个带有异常信息的 Dialog
     * @param context
     * @param threadName
     * @param t
     */
    public static void showExceptionDialog(Context context, String threadName, Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("哦活，掌上重邮崩溃了！");
        final String exceptionString = generateThrowableStackString(t);
        builder.setMessage(generateThrowableStackString(t));
        builder.setPositiveButton("把异常丢给系统", (dialog, which) -> originHandler.uncaughtException(new Thread(threadName), t));
        builder.setNegativeButton("复制异常信息并离开", (dialog, which) -> {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("exception trace stack", exceptionString));
        });
        builder.setOnDismissListener(dialog -> {
            if (context instanceof ExceptionActivity) {
                ((ExceptionActivity) context).finish();
                System.exit(0);
            }
        });
        builder.create().show();
    }

    /**
     * 安装此工具
     * @param applicationContext
     * @param enable 设置为 false 可以禁用此工具
     */
    public static void install(Context applicationContext , boolean enable) {
        ExceptionActivity.originHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (BuildConfig.DEBUG && enable) {
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                Thread.setDefaultUncaughtExceptionHandler(originHandler);
                start(applicationContext, t, e);
                System.exit(0);
            });
        }
    }

}
