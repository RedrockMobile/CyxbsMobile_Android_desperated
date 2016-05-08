package com.mredrock.cyxbs.network.func;

import android.util.Log;

import io.rx_cache.Reply;
import rx.functions.Func1;

/**
 * Created by cc on 16/4/28.
 */
public class CacheMapFunc<T> implements Func1<Reply<T>, T> {

    @Override
    public T call(Reply<T> reply) {
        Log.d("CacheMapFunc", "source: " + reply.getSource());
        return reply.getData();
    }
}
