package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.Topic;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.adapter.TopicHeaderAdapter;
import com.mredrock.cyxbs.ui.fragment.social.TopicFragment;
import com.mredrock.cyxbs.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simonla on 2017/4/5.
 * 19:14
 */

public class HeaderViewWrapper {

    public static final String TAG = HeaderViewWrapper.class.getSimpleName();

    @Bind(R.id.rv_topic_header)
    EasyRecyclerView mRvTopicHeader;
    private View mView;

    public HeaderViewWrapper(ViewGroup parent, Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.item_topic_header, parent, false);
        ButterKnife.bind(this, mView);
        mRvTopicHeader.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mRvTopicHeader.addItemDecoration(new SpaceDecoration((int) Utils.dp2Px(context,10)));
        TopicHeaderAdapter topicHeaderAdapter = new TopicHeaderAdapter(context);
        mRvTopicHeader.setAdapter(topicHeaderAdapter);
        topicHeaderAdapter.addFooter(new Header());
        User user = APP.getUser(context);
        ArrayList<Topic> list = new ArrayList<>();
        RequestManager.getInstance().getTopicList(new SimpleSubscriber<>(context, new SubscriberListener<List<Topic>>() {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(List<Topic> topics) {
                super.onNext(topics);
                topicHeaderAdapter.addAll(topics);
                list.addAll(topics);
                Log.d(TAG, "onNext: "+topics.toString());
            }
        }), 10, 0, user.stuNum, user.idNum, TopicFragment.TopicType.ALL_TOPIC);
        topicHeaderAdapter.setOnItemClickListener(position -> TopicArticleActivity.start(context,list.get(position).getTopic_id()));
    }

    public View getView() {
        return mView;
    }

    private class Header implements RecyclerArrayAdapter.ItemView{

        @Override
        public View onCreateView(ViewGroup parent) {
           return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_bbdd_footer, parent, false);
        }

        @Override
        public void onBindView(View headerView) {
            headerView.setOnClickListener(v -> {
                Intent intent = new Intent(headerView.getContext(), TopicActivity.class);
                headerView.getContext().startActivity(intent);
            });
        }
    }
}
