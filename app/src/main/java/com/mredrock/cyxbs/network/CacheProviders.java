package com.mredrock.cyxbs.network;

import com.mredrock.cyxbs.model.AboutMe;
import com.mredrock.cyxbs.model.Exam;
import com.mredrock.cyxbs.model.Grade;
import com.mredrock.cyxbs.model.social.HotNews;

import java.util.List;

import io.rx_cache.DynamicKey;
import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by mathiasluo on 16-4-17.
 */
public interface CacheProviders {

    Observable<Reply<List<AboutMe>>> getCacheRelateMes(Observable<List<AboutMe>> oRelateMes, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<HotNews>>> getMyTrend(Observable<List<HotNews>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<Grade>>> getCacheGradeList(Observable<List<Grade>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<Exam>>> getCacheExamList(Observable<List<Exam>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<HotNews>>> getCacheNews(Observable<List<HotNews>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<HotNews>>> getCacheContentBean(Observable<List<HotNews>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

}
