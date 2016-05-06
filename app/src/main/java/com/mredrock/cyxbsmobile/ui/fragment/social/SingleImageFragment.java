package com.mredrock.cyxbsmobile.ui.fragment.social;

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
import com.mredrock.cyxbsmobile.ui.fragment.BaseLazyFragment;
import com.mredrock.cyxbsmobile.util.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by mathiasluo on 16-4-16.
 */
public class SingleImageFragment extends BaseLazyFragment implements PhotoViewAttacher.OnPhotoTapListener {


    @Bind(R.id.fragment_progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.image_shot)
    ImageView mImageView;
    @Bind(R.id.layout)
    RelativeLayout layout;
    PhotoViewAttacher mAttacher;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = getArguments().getString("url");
        View view = inflater.inflate(R.layout.content_img, container, false);
        ButterKnife.bind(this, view);
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
    protected void onFirstUserVisible() {
        ImageLoader.getInstance()
                .loadImageWithTargetView(url, new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (mImageView == null) return;
                        mImageView.setImageBitmap(resource);
                        closeProgress();
                        mAttacher = new PhotoViewAttacher(mImageView);
                        mAttacher.update();
                        mAttacher.setOnPhotoTapListener(SingleImageFragment.this);
                    }
                });
    }

    @Override
    public void onPhotoTap(View view, float v, float v1) {
        getActivity().finish();
    }

    @Override
    public void onOutsidePhotoTap() {
        getActivity().finish();
    }
}

