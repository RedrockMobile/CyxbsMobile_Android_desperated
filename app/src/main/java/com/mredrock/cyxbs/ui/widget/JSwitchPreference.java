package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.preference.TwoStatePreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.suke.widget.SwitchButton;

/**
 * Created by Jay on 2017/7/31.
 */

public class JSwitchPreference extends TwoStatePreference {
    public JSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        View checkableView = view.findViewById(android.R.id.checkbox);
        if (checkableView != null && checkableView instanceof Checkable) {
            if (checkableView instanceof SwitchButton) {
                final SwitchButton switchView = (SwitchButton) checkableView;
                switchView.setOnCheckedChangeListener(null);
            }

            ((Checkable) checkableView).setChecked(isChecked());

            if (checkableView instanceof SwitchButton) {
                final SwitchButton switchView = (SwitchButton) checkableView;
                switchView.setOnCheckedChangeListener((view1, isChecked) -> {
                    if (!callChangeListener(isChecked)) {
                        switchView.setChecked(!isChecked);
                        return;
                    }

                    JSwitchPreference.this.setChecked(isChecked);
                });
            }
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
    }
}
