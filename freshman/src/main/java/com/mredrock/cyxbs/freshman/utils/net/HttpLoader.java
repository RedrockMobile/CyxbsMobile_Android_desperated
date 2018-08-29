package com.mredrock.cyxbs.freshman.utils.net;


import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mredrock.cyxbs.freshman.utils.net.Const.BASE_URL;

/**
 * 用于加载网络
 */
public class HttpLoader {

    public static APIService service = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(APIService.class);

    public static <T> Disposable get(ApiChooser<T> chooser, Consumer<T> item, Consumer<? super Throwable> error) {
        return chooser.getObservable(service)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item, error);
    }

    @FunctionalInterface
    public interface ApiChooser<T> {
        Observable<T> getObservable(APIService service);
    }

}
