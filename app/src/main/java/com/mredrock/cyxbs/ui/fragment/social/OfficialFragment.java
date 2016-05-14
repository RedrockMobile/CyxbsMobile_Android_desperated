package com.mredrock.cyxbs.ui.fragment.social;


import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;

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
        holder.mTextContent.setText(hotNewsContent.officeNewsContent.title);
        holder.mTextName.setText(hotNewsContent.officeNewsContent.getOfficeName());
        holder.enableAvatarClick = false;

//        Log.e("----->>>", hotNewsContent.content.toString());
        holder.mImgAvatar.setImageResource(R.drawable.ic_official_notification);

     /*   //暂停点赞
        holder.mBtnFavor.setOnClickListener(null);
        holder.mBtnFavor.setCompoundDrawablesWithIntrinsicBounds(holder.mBtnFavor.getResources().getDrawable(R.drawable.ic_support_unlike), null, null, null);*/
    }

}