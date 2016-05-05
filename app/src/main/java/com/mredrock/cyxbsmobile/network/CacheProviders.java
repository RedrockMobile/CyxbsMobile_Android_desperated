package com.mredrock.cyxbsmobile.network;

import com.mredrock.cyxbsmobile.model.community.HotNews;

import java.util.List;

import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by mathiasluo on 16-4-17.
 */
public interface CacheProviders {

    Observable<Reply<List<HotNews>>> getCacheNews(Observable<List<HotNews>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<HotNews>>> getCacheContentBean(Observable<List<HotNews>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);
}
