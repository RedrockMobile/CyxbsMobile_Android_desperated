package com.mredrock.cyxbsmobile.ui.fragment.community;

import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;

import java.util.List;

import rx.Observable;

/**
 * Created by mathiasluo on 16-4-26.
 */
public class OfficialFragment extends BaseNewsFragment {

    @Override
    Observable<List<News>> provideData(int size, int page, boolean update) {
        return RequestManager.getInstance().getListNews(size, page, update);
    }

    @Override
    Observable<List<News>> provideData(int size, int page) {
        return RequestManager.getInstance().getListNews(size, page);
    }

    @Override
    protected void setDate(NewsAdapter.ViewHolder holder, News.DataBean mDataBean) {
        super.setDate(holder, mDataBean);
        holder.mTextContent.setText(mDataBean.getContentBean().getTitle());
        holder.mTextName.setText(mDataBean.getContentBean().getUnit() != "" ? mDataBean.getContentBean().getUnit() : "教务在线");

    }
}