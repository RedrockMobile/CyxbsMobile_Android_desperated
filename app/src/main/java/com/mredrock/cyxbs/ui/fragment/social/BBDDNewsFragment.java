package com.mredrock.cyxbs.ui.fragment.social;

import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;
import com.mredrock.cyxbs.util.RxBus;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by mathiasluo on 16-4-26.
 */
public class BBDDNewsFragment extends BaseNewsFragment {

    private Subscription mSubscription;

    @Override
    void provideData(Subscriber<List<HotNews>> subscriber, int size, int page) {
        RequestManager.getInstance().getListArticle(subscriber, BBDDNews.BBDD, size, page);
    }

    @Override
    protected void setDate(NewsAdapter.NewsViewHolder holder, HotNewsContent hotNewsContent) {
        super.setDate(holder, hotNewsContent);
        holder.enableAvatarClick = !(hotNewsContent.user_id.equals("0") || hotNewsContent.typeId <
                5 || hotNewsContent.typeId == 7);
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
                    getCurrentData(BaseNewsFragment.PER_PAGE_NUM, BaseNewsFragment.FIRST_PAGE_INDEX);
                    mRecyclerView.scrollToPosition(0);
                });
    }

    private void unregisterObservable() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterObservable();
    }

    @Override
    public void initAdapter(List<HotNews> listHotNews) {
        super.initAdapter(listHotNews);
        addHeaderView();
    }
}
