package com.mredrock.cyxbs.ui.fragment.social;

import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;
import com.mredrock.cyxbs.util.RxBus;

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
        if (hotNewsContent.user_id.equals("0") || hotNewsContent.typeId < 5) {
            holder.enableAvatarClick = false;
        } else {
            holder.enableAvatarClick = true;
        }
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
                    getCurrentData(BaseNewsFragment.PER_PAGE_NUM, BaseNewsFragment.FIRST_PAGE_INDEX, true);
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
}
