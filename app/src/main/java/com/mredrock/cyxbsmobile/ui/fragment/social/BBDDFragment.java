package com.mredrock.cyxbsmobile.ui.fragment.social;

import com.mredrock.cyxbsmobile.model.community.BBDDNews;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.util.RxBus;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by mathiasluo on 16-4-26.
 */
public class BBDDFragment extends BaseNewsFragment {

    private Subscription mSubscription;

    @Override
    Observable<List<News>> provideData(int size, int page, boolean update) {
        return RequestManager.getInstance().getListArticle(BBDDNews.BBDD, size, page, update);
    }

    @Override
    Observable<List<News>> provideData(int size, int page) {
        return RequestManager.getInstance().getListArticle(BBDDNews.BBDD, size, page);
    }

    @Override
    protected void init() {
        super.init();
        registerObservable();
    }

    private void registerObservable() {
        mSubscription = RxBus.getDefault()
                .toObserverable(News.class)
                .subscribe(s -> {
                    ((CommunityContainerFragment) getParentFragment()).changeViewPagerIndex(1);
                    mNewsAdapter.addToFirst(s);
                }, throwable -> {

                });
    }

    private void unregisterObservable() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterObservable();
    }
}
