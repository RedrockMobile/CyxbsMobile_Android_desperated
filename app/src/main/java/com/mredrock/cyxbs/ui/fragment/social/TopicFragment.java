package com.mredrock.cyxbs.ui.fragment.social;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.mredrock.cyxbs.ui.activity.social.TopicArticleActivity;
import com.mredrock.cyxbs.ui.adapter.topic.TopicAdapter;
import com.mredrock.cyxbs.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TopicFragment extends Fragment implements RecyclerArrayAdapter.OnMoreListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_TYPE = "topic_type";
    @Bind(R.id.rv_topic)
    EasyRecyclerView mRvTopic;
    private RecyclerArrayAdapter<Topic> mAdapter;
    private int mPage = 0;
    private String mType;
    private List<Topic> mTopics = new ArrayList<>();

    public TopicFragment() {
    }

    public static TopicFragment newInstance(String type) {
        TopicFragment topicFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TYPE, type);
        topicFragment.setArguments(bundle);
        return topicFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(EXTRA_TYPE);
        }
    }

    public void getTopic() {
        User user = APP.getUser(getContext());
        RequestManager.getInstance().getTopicList(new SimpleSubscriber<>(getContext(),
                false, new SubscriberListener<List<Topic>>() {
            @Override
            public boolean onError(Throwable e) {
                if (mRvTopic != null && mRvTopic.getSwipeToRefresh() != null)
                    mRvTopic.setRefreshing(false);
                return super.onError(e);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (mRvTopic != null && mRvTopic.getSwipeToRefresh() != null)
                    mRvTopic.setRefreshing(false);
            }

            @Override
            public void onNext(List<Topic> topics) {
                super.onNext(topics);
                if (topics.size() == 0 && mPage == 0) {
                    mRvTopic.showEmpty();
                } else {
                    mAdapter.addAll(topics);
                    mTopics.addAll(topics);
                }
            }
        }), 10, mPage, user.stuNum, user.idNum, mType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRvTopic.setAdapterWithProgress(mAdapter = new TopicAdapter(getContext()));
        layoutManager.setSpanSizeLookup(mAdapter.obtainGridSpanSizeLookUp(2));
        mRvTopic.setLayoutManager(layoutManager);
        SpaceDecoration spaceDecoration = new SpaceDecoration((int) Utils.dp2Px(getContext(), 16));
        mRvTopic.addItemDecoration(spaceDecoration);
        mRvTopic.setRefreshListener(this);
        mRvTopic.setRefreshingColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        onRefresh();
        mAdapter.setMore(R.layout.item_topic_more, this);
        mAdapter.setNoMore(R.layout.item_topic_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                mAdapter.resumeMore();
            }

            @Override
            public void onNoMoreClick() {
                mAdapter.resumeMore();
            }
        });
        mAdapter.setOnItemClickListener(position -> TopicArticleActivity.
                start(getContext(), mTopics.get(position).getTopic_id()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        mRvTopic.setRefreshing(true);
        mPage = 0;
        mAdapter.clear();
        getTopic();
    }

    @Override
    public void onMoreShow() {
        mPage++;
        getTopic();
    }

    @Override
    public void onMoreClick() {

    }

    public static class TopicType {
        public static final String MY_TOPIC = "my_topic";
        public static final String ALL_TOPIC = "all_topic";
        public static final String SEARCH_TOPIC = "search_topic";
    }
}
