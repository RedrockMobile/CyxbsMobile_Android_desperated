package com.mredrock.cyxbs.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.explore.ExploreSchoolCarActivity;

/**
 * Created by glossimar on 2017/12/26.
 */

public class ExploreSchoolCarDialog {
    AlertDialog dialog;

    public void show(Activity activity, int type) {
        LayoutInflater inflater = activity.getLayoutInflater();
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_explore_school_car_notserve, null);
        ImageButton diasmissButton = layout.findViewById(R.id.school_car_dialog_dismiss_button);
        ImageButton negativeButton = layout.findViewById(R.id.school_car_dialog_negative_button);
        ImageButton positiveButton = layout.findViewById(R.id.school_car_dialog_positive_button);

        dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setView(layout)
                .create();
        dialog.setOnDismissListener(listener -> activity.finish());

        diasmissButton.setOnClickListener(v -> {
            if (v.getId() == R.id.school_car_dialog_dismiss_button) {
                dialog.dismiss();
                activity.finish();
            }
        });
        try {
            WindowManager manager = activity.getWindowManager();
            int width = manager.getDefaultDisplay().getWidth();
            int height = manager.getDefaultDisplay().getHeight();

            Window dialogWindow =  dialog.getWindow();
            dialog.show();
            dialogWindow.setLayout(width*4/5, height*7/11);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            switch (type) {
                case ExploreSchoolCarActivity.LOST_SERVICES:
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_school_car_not_serve);
                    break;
                case ExploreSchoolCarActivity.TIME_OUT:
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_school_car_search_time_out);
                    break;
                case ExploreSchoolCarActivity.NO_GPS:
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_school_car_search_no_gps);
                    negativeButton.setOnClickListener(v -> {
                        dialog.cancel();
                    });
                    positiveButton.setOnClickListener(v -> {
                        dialog.cancel();
                    });
                    break;
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void cancleDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            dialog = null;
        }
    }
}
