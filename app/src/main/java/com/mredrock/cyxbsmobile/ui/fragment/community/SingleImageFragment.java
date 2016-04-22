package com.mredrock.cyxbsmobile.ui.fragment.community;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.util.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-4-16.
 */
public class SingleImageFragment extends BaseLazyFragment implements View.OnClickListener {

    @Bind(R.id.fragment_progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.image_shot)
    ImageView mImageView;
    @Bind(R.id.layout)
    RelativeLayout layout;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = getArguments().getString("url");
        View view = inflater.inflate(R.layout.content_img, container, false);
        ButterKnife.bind(this, view);
        layout.setOnClickListener(this);
        showProgress();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void closeProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    void onFirstUserVisible() {
        // ImageLoader.getInstance().loadImage(url, mImageView);
        ImageLoader.getInstance().loadImageWithTargetView(url, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mImageView.setImageBitmap(resource);
                closeProgress();
            }
        });
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }
}
