package com.excitingboat.freshmanspecial.model.net;

import com.excitingboat.freshmanspecial.config.Config;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PinkD on 2016/8/4.
 * Retrofit
 */
public class SingleRetrofit {
    Retrofit retrofit;

    private static class Holder {
        private static SingleRetrofit singleRetrofit = new SingleRetrofit();
    }

    public static SingleRetrofit getInstance() {
        return Holder.singleRetrofit;
    }

    public SingleRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
