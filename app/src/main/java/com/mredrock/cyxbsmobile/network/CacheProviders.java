package com.mredrock.cyxbsmobile.network;

import com.mredrock.cyxbsmobile.model.AboutMe;
import com.mredrock.cyxbsmobile.model.Exam;
import com.mredrock.cyxbsmobile.model.Grade;
import com.mredrock.cyxbsmobile.model.community.News;

import io.rx_cache.DynamicKey;
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

    Observable<Reply<List<AboutMe>>> getCacheRelateMes(Observable<List<AboutMe>> oRelateMes, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<News>>> getCacheContentBean(Observable<List<News>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<News>>> getMyTrend(Observable<List<News>> oNews, DynamicKey key,EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<Grade>>> getCacheGradeList(Observable<List<Grade>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<Exam>>> getCacheExamList(Observable<List<Exam>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);
}
