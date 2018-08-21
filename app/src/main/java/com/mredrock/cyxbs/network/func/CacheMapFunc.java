package com.mredrock.cyxbs.network.func;

import android.util.Log;

import io.reactivex.functions.Function;
import io.rx_cache2.Reply;


/**
 * Created by cc on 16/4/28.
 */
public class CacheMapFunc<T> implements Function<Reply<T>, T> {

    @Override
    public T apply(Reply<T> reply) throws Exception {
        Log.d("CacheMapFunc", "source: " + reply.getSource());
        return reply.getData();
    }
}
