package com.mredrock.cyxbs.freshman.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.MienStu;
import com.mredrock.cyxbs.freshman.utils.DensityUtils;
import com.mredrock.cyxbs.freshman.utils.net.Const;

@SuppressLint("ValidFragment")
public class CquptMienStuFragment extends Fragment {

    private MienStu.MienDetailBean bean;
    private View parent;
    private RoundedImageView img;
    private TextView tv;
    private ImageView seeMore;
    private LinearLayout linearLayout;
    private boolean isSeeMore = false;


    public void setBean(MienStu.MienDetailBean bean) {
        this.bean = bean;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.freshman_fragment_cqupt_mien_stu_detail, container, false);
        init();
        return parent;
    }

    private void init() {
        img = parent.findViewById(R.id.freshman_CyMien_detail_img);
        tv = parent.findViewById(R.id.freshman_CyMien_detail_desc);
        TextView name = parent.findViewById(R.id.freshman_CyMien_detail_name);
        seeMore = parent.findViewById(R.id.freshman_CyMien_detail_seeMore);
        linearLayout = parent.findViewById(R.id.freshman_CyMien_detail_parent);

        if (!bean.getName().equals("红岩网校工作站")) {
            Glide.with(getContext())
                    .load(Const.IMG_BASE_URL + bean.getPicture().get(0))
                    .thumbnail(0.1f)
                    .into(img);
        } else {
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext())
                    .load(Const.IMG_BASE_URL + bean.getPicture().get(0))
                    .thumbnail(0.1f)
                    .into(img);
        }

        tv.setText(bean.getContent());
        tv.setLineSpacing(0, 1.5f);

        name.setText(bean.getName());
        tv.setLines(6);

        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.height = DensityUtils.getScreenHeight(getActivity()) / 4;
        img.setLayoutParams(layoutParams);

        linearLayout.setOnClickListener(v -> {
            if (isSeeMore) {
                tv.setMaxLines(6);
                seeMore.setBackgroundResource(R.drawable.freshman_icon_report_more);
                isSeeMore = false;
            } else {
                tv.setMaxLines(500);
                seeMore.setBackgroundResource(R.drawable.freshman_icon_report_simple);
                isSeeMore = true;
            }
        });

        tv.setOnClickListener(v -> {
            if (isSeeMore) {
                tv.setMaxLines(6);
                seeMore.setBackgroundResource(R.drawable.freshman_icon_report_more);
                isSeeMore = false;
            } else {
                tv.setMaxLines(500);
                seeMore.setBackgroundResource(R.drawable.freshman_icon_report_simple);
                isSeeMore = true;
            }
        });

    }

}
