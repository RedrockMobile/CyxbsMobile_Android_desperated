package com.mredrock.freshmanspecial.view.dataFragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.view.BarGraphView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmploymentDataFragment extends Fragment {
    private boolean isAnimation = false;

    private String[] mCompanyNames = new String[]{
            "腾讯", "华为", "烽火",
            "中国铁塔", "海信", "猪八戒",
            "中国联通", "深信服", "中国移动",
            "中国电信"
    };

    private int[] mValues = new int[]{20, 22, 23, 40, 35, 65, 194, 25, 177, 123};

    private List<BarGraphView> mBars = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = LayoutInflater.from(getActivity())
                .inflate(R.layout.special_2017_fragment_employment_data, null, false);
        ViewGroup myContainer = (ViewGroup) parent.findViewById(R.id.container);

        for (int i = 0; i < mCompanyNames.length; i++) {
            ViewGroup itemView = (ViewGroup) LayoutInflater.from(getActivity())
                    .inflate(R.layout.special_2017_item_fragment_employment_data, null, false);
            TextView name = (TextView) itemView.findViewById(R.id.commpany);
            name.setText(mCompanyNames[i]);
            BarGraphView bar = (BarGraphView) itemView.findViewById(R.id.bar);
            bar.setValue(mValues[i]);
            mBars.add(bar);
            myContainer.addView(itemView);
        }
        myContainer.getChildAt(0).setPadding(0, dp2px(22), 0, 0);
        myContainer.getChildAt(myContainer.getChildCount() - 1)
                .setPadding(0, dp2px(30), 0, dp2px(22));
        startAnimation();

        return parent;
    }

    public void startAnimation() {
        if (isAnimation) {
            return;
        }
        for (BarGraphView bar : mBars) {
            ObjectAnimator animator = ObjectAnimator.ofInt(bar, "value", 0, bar.getValue());
            animator.setDuration(2000 * bar.getValue() / 194);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isAnimation = false;
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isAnimation = true;
                }
            });
            animator.start();
        }
    }

    private int dp2px(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
