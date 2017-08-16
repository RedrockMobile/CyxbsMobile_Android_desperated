package com.mredrock.freshmanspecial.view.JunxunFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.ScreenUnit;

/**
 * 军训图片点开后的幻灯片
 */
public class SlideFragment extends Fragment {

    private String title,url;
    private View view;
    private ImageView imageView;
    //private TextView textView;
    private int screenWidth = 0;

    public SlideFragment() {
        super();
    }

    public SlideFragment(String title,String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(screenWidth==0){
            screenWidth = ScreenUnit.bulid(getContext()).getPxWide();
        }
        imageView = (ImageView) view.findViewById(R.id.slide_image);
        Glide.with(getActivity()).load(url).into(imageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.special_2017_fragment_slide, container, false);
        return view;
    }

}
