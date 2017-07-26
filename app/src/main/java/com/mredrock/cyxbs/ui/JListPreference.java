package com.mredrock.cyxbs.ui;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.ui.widget.PickerBottomSheetDialog;

/**
 * Created by Jay on 2017/7/26.
 */

public class JListPreference extends ListPreference {
    private String[] mEntries;

    public JListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        CharSequence[] charSequences = getEntries();
        mEntries = new String[charSequences.length];
        for (int i = 0; i < charSequences.length; i++) {
            mEntries[i] = charSequences[i].toString();
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        PickerBottomSheetDialog dialog = new PickerBottomSheetDialog(getContext());
        dialog.setData(mEntries);
        dialog.setPosition(findIndexOfValue(getValue()));
        dialog.setOnClickListener(new PickerBottomSheetDialog.OnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String value, int position) {
                String val = getEntryValues()[position].toString();
                if (callChangeListener(val)) {
                    setValue(val);
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        setEnabledStateOnViews(view, isEnabled());
    }

    private void setEnabledStateOnViews(View v, boolean enabled) {
        v.setEnabled(enabled);
        if (v instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) v;
            for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                setEnabledStateOnViews(vg.getChildAt(i), enabled);
            }
        } else {
            v.setAlpha(enabled ? 1.0f : 0.5f);
        }
    }
}
