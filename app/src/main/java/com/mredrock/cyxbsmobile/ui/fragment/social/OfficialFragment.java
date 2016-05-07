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
public class OfficialFragment extends BaseNewsFragment {

    @Override
    Observable<List<HotNews>> provideData(int size, int page, boolean update) {
        return RequestManager.getInstance().getListNews(size, page, update);
    }

    @Override
    Observable<List<HotNews>> provideData(int size, int page) {
        return RequestManager.getInstance().getListNews(size, page);
    }

    @Override
    protected void setDate(NewsAdapter.ViewHolder holder, HotNewsContent hotNewsContent) {
        super.setDate(holder, hotNewsContent);
        holder.mTextContent.setText(hotNewsContent.content.title);
        holder.mTextName.setText(hotNewsContent.content.getOfficeName());
        holder.enableAvatarClick = false;
    }

}