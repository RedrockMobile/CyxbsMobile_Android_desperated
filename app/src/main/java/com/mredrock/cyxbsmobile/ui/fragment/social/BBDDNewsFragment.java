package com.mredrock.cyxbsmobile.ui.fragment.social;

import com.mredrock.cyxbsmobile.model.social.BBDDNews;
import com.mredrock.cyxbsmobile.model.social.HotNews;
import com.mredrock.cyxbsmobile.model.social.HotNewsContent;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;
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
    protected void setDate(NewsAdapter.ViewHolder holder, HotNewsContent hotNewsContent) {
        super.setDate(holder, hotNewsContent);
        if (hotNewsContent.user_id.equals("0") || hotNewsContent.type_id < 5)
            holder.enableClick = false;
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
                    ((SocialContainerFragment) getParentFragment()).changeViewPagerIndex(1);
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
