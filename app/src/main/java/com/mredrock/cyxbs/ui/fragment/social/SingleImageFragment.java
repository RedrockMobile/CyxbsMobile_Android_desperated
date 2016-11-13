package com.mredrock.cyxbs.ui.fragment.social;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.fragment.BaseLazyFragment;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.SaveImageUtils;
import com.mredrock.cyxbs.util.permission.AfterPermissionGranted;
import com.mredrock.cyxbs.util.permission.EasyPermissions;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by mathiasluo on 16-4-16.
 */
public class SingleImageFragment extends BaseLazyFragment implements PhotoViewAttacher.OnPhotoTapListener, EasyPermissions.PermissionCallbacks {


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
                        onUserVisible();

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
    public void onUserVisible() {
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("保存图片")
                        .setMessage("您确定保存此图？会耗费您一点点流量哟~")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mImageView.setDrawingCacheEnabled(true);
                                if (mImageView != null){
                                    SaveImageUtils.imageSave(mImageView,url,which,getContext());
                                }
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getContext(),"抱歉，您没有开启权限保存图片哦~",Toast.LENGTH_SHORT).show();

    }
}

