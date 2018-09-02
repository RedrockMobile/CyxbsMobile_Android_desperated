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
public class HotNewsFragment extends BaseNewsFragment {

    void provideData(Observer<List<HotNews>> observer, int size, int page) {
        RequestManager.getInstance().getHotArticle(observer, size, page);
    }


    @Override
    protected void setDate(NewsAdapter.NewsViewHolder holder, HotNewsContent hotNewsContent) {
        super.setDate(holder, hotNewsContent);
        holder.enableAvatarClick = !(hotNewsContent.user_id.equals("0") || hotNewsContent.typeId < 5);
    }
}
