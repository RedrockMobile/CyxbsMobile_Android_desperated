package com.mredrock.cyxbs.ui.fragment.explore.eletric;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ：AceMurder
 * Created on ：2017/2/26
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class DialogRemindFragment extends DialogFragment {

    @Bind(R.id.tv_dialog_remind_electric)
    TextView textView;
    private String remindText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       // setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        return super.onCreateDialog(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_dormitory_setting_remind,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        remindText = getArguments().getString("REMIND_TEXT");
        if (remindText != null)
            textView.setText(remindText);

    }


    @OnClick(R.id.btn_dormitory_dialog_confirm)
    public void onOKClick(){
        dismiss();
    }

}