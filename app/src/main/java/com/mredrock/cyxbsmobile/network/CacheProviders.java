package com.mredrock.cyxbsmobile.network;

import com.mredrock.cyxbsmobile.model.community.ContentBean;
import com.mredrock.cyxbsmobile.model.community.News;

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

    Observable<Reply<List<ContentBean>>> getCacheContentBean(Observable<List<ContentBean>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

}
