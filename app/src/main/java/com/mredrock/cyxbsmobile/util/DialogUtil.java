package com.mredrock.cyxbsmobile.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbsmobile.R;


/**
 * Created by mathiasluo on 16-4-16.
 */
public class DialogUtil {
    private static MaterialDialog dialog;

    public static final void showLoadingDiaolog(Context context, String title) {
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content("请稍候")
                .theme(Theme.LIGHT)
                .backgroundColor(context.getResources().getColor(R.color.white))
                .progress(true, 100)
                .cancelable(false)
                .show();
    }


    public static final void showLoadSucess(Context context, String title, String content, String positiveText, String negativeText, DialogListener dialogListener) {
        dialog = new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        if (dialogListener != null) dialogListener.onPositive();
                    }
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        if (dialogListener != null) dialogListener.onNegative();
                    }
                })
                .show();
    }

    public static final void dismissDialog() {
        dialog.dismiss();
    }

    public interface DialogListener {

        void onPositive();

        void onNegative();
    }

}
