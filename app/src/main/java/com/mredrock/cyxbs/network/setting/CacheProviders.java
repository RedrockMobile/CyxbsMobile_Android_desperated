package com.mredrock.cyxbs.network.setting;

import com.mredrock.cyxbs.model.AboutMe;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.Exam;
import com.mredrock.cyxbs.model.Grade;
import com.mredrock.cyxbs.model.social.HotNews;

import java.util.List;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;

/**
 * Created by mathiasluo on 16-4-17.
 */
public interface CacheProviders {

    Observable<Reply<List<AboutMe>>> getCachedRelateMes(Observable<List<AboutMe>> oRelateMes, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<HotNews>>> getCachedMyTrend(Observable<List<HotNews>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<Grade>>> getCachedGradeList(Observable<List<Grade>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<Exam>>> getCachedExamList(Observable<List<Exam>> oNews, DynamicKey key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<HotNews>>> getCachedNews(Observable<List<HotNews>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<HotNews>>> getCachedContentBean(Observable<List<HotNews>> oNews, DynamicKeyGroup key, EvictDynamicKey evictDynamicKey);

    Observable<Reply<List<Course>>> getCachedCourseList(Observable<List<Course>> oCourseList, DynamicKey key, EvictDynamicKey evictDynamicKey);
}
