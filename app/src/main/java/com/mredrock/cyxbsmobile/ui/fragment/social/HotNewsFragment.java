package com.mredrock.cyxbsmobile.ui.fragment.social;


import com.mredrock.cyxbsmobile.model.social.HotNews;
import com.mredrock.cyxbsmobile.model.social.HotNewsContent;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;

import java.util.List;

import rx.Observable;

/**
 * Created by mathiasluo on 16-4-26.
 */
public class HotNewsFragment extends BaseNewsFragment {

    @Override
    Observable<List<HotNews>> provideData(int size, int page, boolean update) {
        return RequestManager.getInstance().getHotArticle(size, page, update);
    }

    @Override
    Observable<List<HotNews>> provideData(int size, int page) {
        return RequestManager.getInstance().getHotArticle(size, page);
    }

    @Override
    protected void setDate(NewsAdapter.ViewHolder holder, HotNewsContent hotNewsContent) {
        super.setDate(holder, hotNewsContent);
        if (hotNewsContent.user_id.equals("0") || hotNewsContent.type_id < 5)
            holder.enableAvatarClick = false;
        else holder.enableAvatarClick = true;
    }
}
