package com.mredrock.cyxbs.component.task.progress;

import android.app.ProgressDialog;
import android.content.Context;

import android.os.Handler;
import android.os.Message;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by cc on 16/3/19.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG    = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private MaterialDialog pd;

    private Context                context;
    private boolean                cancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog() {
        if (pd == null) {
            pd = new MaterialDialog.Builder(context)
                    .progress(true, 0)
                    .build();
            pd.setContent("Loading...");

            pd.setCancelable(cancelable);

            if (cancelable) {
                pd.setOnCancelListener(dialogInterface -> mProgressCancelListener.onCancelProgress());
            }

            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
