package com.excitingboat.freshmanspecial.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.excitingboat.freshmanspecial.config.Config;

/**
 * Created by PinkD on 2016/6/19.
 * For Glide
 */
public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(Config.cacheDir.toString(), 20 * Config.MB));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
