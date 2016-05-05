package com.mredrock.cyxbsmobile.ui.fragment.social;

import com.mredrock.cyxbsmobile.model.community.BBDDNews;
import com.mredrock.cyxbsmobile.model.community.HotNews;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.util.RxBus;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by mathiasluo on 16-4-26.
 */
public class BBDDNewsFragment extends BaseNewsFragment {

    private Subscription mSubscription;

    @Override
    Observable<List<HotNews>> provideData(int size, int page, boolean update) {
        return RequestManager.getInstance().getListArticle(BBDDNews.BBDD, size, page, update);

    }

    @Override
    Observable<List<HotNews>> provideData(int size, int page) {
        return RequestManager.getInstance().getListArticle(BBDDNews.BBDD, size, page);

    }

    @Override
    protected void init() {
        super.init();
        registerObservable();
    }

    private void registerObservable() {
        mSubscription = RxBus.getDefault()
                .toObserverable(HotNews.class)
                .subscribe(s -> {
                    ((CommunityContainerFragment) getParentFragment()).changeViewPagerIndex(1);
                    //注释掉的这句话是把 最新发送的推到顶部
                    //mNewsAdapter.addToFirst(s);
                    getCurrentData(BaseNewsFragment.PER_PAGE_NUM, 1, true);
                    mRecyclerView.scrollToPosition(0);
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
