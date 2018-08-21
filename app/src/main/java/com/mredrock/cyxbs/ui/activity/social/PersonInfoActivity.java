package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.model.social.PersonInfo;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.ScreenTools;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-5-6.
 */
public class PersonInfoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "PersonInfoActivity";
    public static final String PERSON_AVATAR = "userAvatar";
    public static final String PERSON_NICKNAME = "userNickName";
    public static final String PERSON_USER_ID = "userId";

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String mUserAvatar;
    private String mNickName;
    private String mUserId;
    private List<HotNews> mHotNewsList = null;
    private FooterViewWrapper mFooterViewWrapper;
    protected NewsAdapter mNewsAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private HeaderViewWrapper mHeaderViewWrapper;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        mUserAvatar = getIntent().getStringExtra(PERSON_AVATAR);
        mNickName = getIntent().getStringExtra(PERSON_NICKNAME);
        mUserId = getIntent().getStringExtra(PERSON_USER_ID);
        mHeaderViewWrapper = new HeaderViewWrapper(this, R.layout.list_person_info_header);
        ButterKnife.bind(this);
        mUser = BaseAPP.getUser(this);
        init();
    }


    private void init() {
        initToolbar();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new NewsAdapter(mHotNewsList);
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mNewsAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mHeaderViewWrapper.view);

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(BaseAPP.getContext(), R.color.colorAccent),
                ContextCompat.getColor(BaseAPP.getContext(), R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mHeaderViewWrapper.setData(mUserAvatar, mNickName);

        requestData();
    }

    private void requestData() {

        RequestManager.getInstance().getPersonInfo(new SimpleObserver<>(this, new SubscriberListener<PersonInfo>() {
            @Override
            public void onNext(PersonInfo personInfo) {
                super.onNext(personInfo);
                super.onNext(personInfo);
                mHeaderViewWrapper.setIntroduction(personInfo.getIntroduction(), personInfo.gender);
            }

            @Override
            public void onStart() {
                super.onStart();
                showLoading();
            }
        }), mUserId, mUser.stuNum, mUser.idNum);

        RequestManager.getInstance().getPersonLatestList(new SimpleObserver<>(this, new SubscriberListener<List<HotNews>>() {
            @Override
            public boolean onError(Throwable e) {
                super.onError(e);
                closeLoading();
                getDataFailed(e.toString());
                return true;
            }

            @Override
            public void onNext(List<HotNews> hotNewses) {
                super.onNext(hotNewses);
                if (mHotNewsList == null) {
                    initAdapter(hotNewses);
                    if (hotNewses.size() == 0) mFooterViewWrapper.showLoadingNoData();
                } else mNewsAdapter.replaceDataList(hotNewses);
                closeLoading();
            }
        }), mUserId, mNickName, mUserAvatar);
    }

    private void initAdapter(List<HotNews> datas) {
        mHotNewsList = datas;
        mNewsAdapter = new NewsAdapter(mHotNewsList) {
            @Override
            public void setDate(NewsViewHolder holder, HotNewsContent mDataBean) {
                super.setDate(holder, mDataBean);
                CardView cardView = (CardView) holder.itemView;
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) cardView.getLayoutParams();
                params.setMargins(0, 0, 0, 10);
                cardView.setLayoutParams(params);

                ViewGroup.LayoutParams ps = holder.mImgAvatar.getLayoutParams();
                ps.width = ScreenTools.instance(PersonInfoActivity.this).dip2px(42);
                ps.height = ps.width;
                holder.mImgAvatar.setLayoutParams(ps);
                holder.enableAvatarClick = false;
                holder.isFromPersonInfo = true;
            }
        };
        mHeaderViewRecyclerAdapter.setAdapter(mNewsAdapter);
        addFooterView(mHeaderViewRecyclerAdapter);
    }

    private void addFooterView(HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter) {
        mFooterViewWrapper = new FooterViewWrapper(mRecyclerView);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterViewWrapper.getFooterView());
        mFooterViewWrapper.showLoadingNoMoreData();
        mFooterViewWrapper.onFailedClick(view -> requestData());
    }


    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> PersonInfoActivity.this.finish());
    }

    public static final void StartActivityWithData(Context context, String userAvatar, String nickName, String userId) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        intent.putExtra(PERSON_AVATAR, userAvatar);
        intent.putExtra(PERSON_NICKNAME, nickName);
        intent.putExtra(PERSON_USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    private void showLoading() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
    }

    private void closeLoading() {
        if (mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
    }


    private void getDataFailed(String reason) {
        Toast.makeText(this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
        Log.e(TAG, reason);
    }

    class HeaderViewWrapper {
        View view;
        @BindView(R.id.person_info_avatar)
        ImageView mCircleImageView;
        @BindView(R.id.peron_info_nickname)
        TextView mTextNickName;
        @BindView(R.id.person_info_introduction)
        TextView mTextIntroduction;
        @BindView(R.id.person_info_gender)
        TextView mTextGender;

        public HeaderViewWrapper(Context context, int layoutId) {
            view = LayoutInflater.from(context).inflate(layoutId, null, false);
            ButterKnife.bind(this, view);
        }


        public void setData(String avatar, String nickname) {
            ImageLoader.getInstance().loadAvatar(avatar, mCircleImageView);
            mCircleImageView.setOnClickListener(view -> ImageActivity.startWithData(PersonInfoActivity.this, avatar));
            mTextNickName.setText(nickname);
        }

        public void setIntroduction(String introduction, String gender) {
            mTextIntroduction.setText(introduction);
            if (gender.trim().equals("男")) {
                mTextGender.setTextColor(ContextCompat.getColor(BaseAPP.getContext(), R.color.colorPrimary));
                mTextGender.setText("♂");
            } else {
                mTextGender.setTextColor(ContextCompat.getColor(BaseAPP.getContext(), R.color.pink));
                mTextGender.setText("♀");
            }
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        requestData();
    }

}
