package com.mredrock.cyxbsmobile.network;

import com.mredrock.cyxbsmobile.model.social.News;

import java.util.List;

import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by mathiasluo on 16-4-17.
 */
public interface CacheProviders {

    Observable<Reply<List<News>>> getCacheNews(Observable<List<News>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<News>>> getCacheContentBean(Observable<List<News>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

}
