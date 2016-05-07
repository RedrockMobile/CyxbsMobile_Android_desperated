package com.mredrock.cyxbsmobile.ui.fragment.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.social.Stu;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 社区
 */
public class SocialContainerFragment extends BaseFragment {

    @Bind(R.id.community_TabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.community_ViewPager)
    ViewPager mViewPager;

    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_container, container, false);
        ButterKnife.bind(this, view);
        mUser = APP.getUser(getContext());
        if (mUser == null) return view;
        if (mUser.id == null) getPersonInfoData();
        else {
            new Stu(mUser.name, mUser.stuNum, mUser.idNum, mUser.id);
            init();
        }
        return view;
    }

    private void getPersonInfoData() {
        if (mUser != null) {
            RequestManager.getInstance().getPersonInfo(new SimpleSubscriber<>(getActivity(),
                    new SubscriberListener<User>() {

                        @Override
                        public void onNext(User user) {
                            super.onNext(user);
                            if (user != null) {
                                mUser = User.cloneFromUserInfo(mUser, user);
                                APP.setUser(getActivity(), mUser);
                                new Stu(mUser.name, mUser.stuNum, mUser.idNum, mUser.id);
                                init();
                            }
                        }

                    }), mUser.stuNum, mUser.idNum);
        }
    }


    private void init() {
        List<Fragment> fragmentLIst = new ArrayList<>();

        HotNewsFragment mPopularNewFragment = new HotNewsFragment();
        BBDDNewsFragment mBBLLNewFragment = new BBDDNewsFragment();

        OfficialFragment mOfficialFragment = new OfficialFragment();
        fragmentLIst.add(mPopularNewFragment);
        fragmentLIst.add(mBBLLNewFragment);
        fragmentLIst.add(mOfficialFragment);

        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), fragmentLIst, Arrays.asList(getActivity().getResources().getStringArray(R.array.community_tab_tiles)));

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragmentLIst.size());

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void changeViewPagerIndex(int index) {
        mViewPager.setCurrentItem(index);
    }
}
