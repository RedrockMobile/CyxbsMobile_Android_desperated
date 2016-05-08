package com.mredrock.cyxbs.subscriber;

/**
 * Created by cc on 16/3/20.
 */
public abstract class SubscriberListener<T> {

    public void onStart() {
    }

    public void onCompleted() {
    }

    public void onError(Throwable e) {
    }

    public void onNext(T t) {
    }

}
