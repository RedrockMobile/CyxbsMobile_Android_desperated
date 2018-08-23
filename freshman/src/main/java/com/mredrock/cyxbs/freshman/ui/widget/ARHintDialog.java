package com.mredrock.cyxbs.freshman.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.mredrock.cyxbs.freshman.R;

public class ARHintDialog extends Dialog {

    public ARHintDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freshman_dialog_hint);
        ImageView back = findViewById(R.id.iv_admission_hint_back);

        back.setOnClickListener(v -> ARHintDialog.this.dismiss());

        setCanceledOnTouchOutside(true);
    }
}
