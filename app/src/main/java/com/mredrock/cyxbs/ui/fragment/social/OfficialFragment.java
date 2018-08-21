package com.mredrock.cyxbs.ui.fragment.social;


import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;

import java.util.List;

import io.reactivex.Observer;


/**
 * Created by mathiasluo on 16-4-26.
 */
public class OfficialFragment extends BaseNewsFragment {

    @Override
    void provideData(Observer<List<HotNews>> observer, int size, int page) {
        RequestManager.getInstance().getListNews(observer, size, page);
    }

    @Override
    protected void setDate(NewsAdapter.NewsViewHolder holder, HotNewsContent hotNewsContent) {
        super.setDate(holder, hotNewsContent);
        holder.setData(hotNewsContent, false);
    }

}