package com.mredrock.cyxbs.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.explore.ExploreSchoolCar;
import com.mredrock.cyxbs.util.DensityUtils;

import static com.thefinestartist.utils.service.ServiceUtil.getWindowManager;

/**
 * Created by glossimar on 2017/12/26.
 */

public class ExploreSchoolCarDialog {

    public static void show(Context context, Activity activity, int type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_explore_school_car_notserve, null);
        ImageButton diasmissButton = (ImageButton) layout.findViewById(R.id.school_car_dialog_dismiss_button);
        ImageButton negativeButton = (ImageButton) layout.findViewById(R.id.school_car_dialog_negative_button);
        ImageButton positiveButton = (ImageButton) layout.findViewById(R.id.school_car_dialog_positive_button);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(layout
                        , DensityUtils.dp2px(context, 0), DensityUtils.dp2px(context, 0), DensityUtils.dp2px(context, 0), DensityUtils.dp2px(context, 0))
                .create();

        diasmissButton.setOnClickListener(v -> {
            if (v.getId() == R.id.school_car_dialog_dismiss_button) {
                dialog.dismiss();
                activity.finish();
            }
        });
        dialog.show();
        Window dialogWindow =  dialog.getWindow();
        dialogWindow.setLayout(845, 1070);

        switch (type) {
            case ExploreSchoolCar.LOST_SERVICES:
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_school_car_not_serve);
                break;
            case ExploreSchoolCar.TIME_OUT:
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_school_car_search_time_out);
                break;
            case ExploreSchoolCar.NO_GPS:
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_school_car_search_unhave_gps);
                negativeButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    activity.finish();
                });
                positiveButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    activity.finish();
                });
                break;
        }
    }
}
