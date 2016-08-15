package com.excitingboat.freshmanspecial.view.fragment.Style;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.config.StyleData;

/**
 * Created by PinkD on 2016/8/9.
 * OrganizationFragment
 */
public class OrganizationFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "OrganizationFragment";
    private TextView detail;
    private TextView[] textViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_freshman_special__fragment_style_organization, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textViews = new TextView[]{(TextView) view.findViewById(R.id.org_text_1),
                (TextView) view.findViewById(R.id.org_text_2),
                (TextView) view.findViewById(R.id.org_text_3),
                (TextView) view.findViewById(R.id.org_text_4),
                (TextView) view.findViewById(R.id.org_text_5),
                (TextView) view.findViewById(R.id.org_text_6),
                (TextView)view.findViewById(R.id.org_text_red)
        };
        detail = (TextView) view.findViewById(R.id.org_detail);
        for (TextView textView : textViews) {
            textView.setOnClickListener(this);
        }
        textViews[0].setBackgroundResource(R.color.background);
        detail.setText(Html.fromHtml(StyleData.ORGANIZATION[0])+"/n");
    }

    @Override
    public void onClick(View v) {
        for (TextView textView : textViews) {
            textView.setBackgroundResource(R.color.none);
//            detail.setBackgroundResource(R.color.background);
        }
        v.setBackgroundResource(R.color.background);
        int i = v.getId();
        if (i == R.id.org_text_1) {
            detail.setText(Html.fromHtml(StyleData.ORGANIZATION[0])+"\n");

        } else if (i == R.id.org_text_2) {
            detail.setText(Html.fromHtml(StyleData.ORGANIZATION[1])+"\n");

        } else if (i == R.id.org_text_3) {
            detail.setText(Html.fromHtml(StyleData.ORGANIZATION[2])+"\n");

        } else if (i == R.id.org_text_4) {
            detail.setText(Html.fromHtml(StyleData.ORGANIZATION[3])+"\n");

        } else if (i == R.id.org_text_5) {
            detail.setText(Html.fromHtml(StyleData.ORGANIZATION[4])+"\n");

        } else if (i == R.id.org_text_6) {
            detail.setText(Html.fromHtml(StyleData.ORGANIZATION[5])+"\n");

        } else if (i == R.id.org_text_red){
            detail.setText(StyleData.ORGANIZATION[6]);
        }

    }
}
