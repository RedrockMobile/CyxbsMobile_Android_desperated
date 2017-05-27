package com.mredrock.cyxbs.ui.fragment.lost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.PersonInfo;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.explore.BaseExploreActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.adapter.lost.LostTabPagerAdapter;
import com.mredrock.cyxbs.ui.adapter.lost.LostViewPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wusui on 2017/2/7.
 */

public class LostContainerFragment extends BaseFragment{
    @Bind(R.id.lost_tab_layout)
    TabLayout tab;
    @Bind(R.id.lost_view_pager)
    ViewPager pager;
    private boolean firstLogin = false;
    private int resumeCount = 0;

    private User mUser;

    LostViewPagerAdapter mAdapter;
    private static int mCurrentPosition = 0;
    protected String ARGUMENT_CATEGORY = "CATEGORY";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_container, container, false);
        ButterKnife.bind(this,view);

        mAdapter = new LostViewPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(mAdapter);
        pager.setCurrentItem(0);
        tab.setupWithViewPager(pager, true);
        //init();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserData();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (firstLogin && resumeCount == 1){
            firstLogin = false;
            getUserData();
        }
        ++resumeCount;
    }
    public  static LostContainerFragment newInstance() {
        LostContainerFragment fragment = new LostContainerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    private void getUserData() {
        if (APP.isLogin()) {
            mUser = APP.getUser(getContext());
            if (mUser.id == null) getPersonInfoData();
            //else init();
        }else {
            firstLogin = true;
        }
    }

    public void getPersonInfoData() {
        if (!APP.isLogin()){
            return;
        }
        if (mUser != null){
            RequestManager.getInstance().getPersonInfo(new SimpleSubscriber<>(getActivity(), new SubscriberListener<PersonInfo>() {
                @Override
                public void onNext(PersonInfo personInfo) {
                    super.onNext(personInfo);
                    super.onNext(personInfo);
                    mUser = User.cloneFromUserInfo(mUser, personInfo);
                    APP.setUser(getActivity(), mUser);
                }
            }), mUser.stuNum, mUser.stuNum, mUser.idNum);
        }
    }


    private void init() {
       /*    List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0;i < 9;i++){
            fragmentList.add(new LostFragment());
        }
     TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), fragmentList, Arrays
                .asList(getResources().getStringArray(R.array.lost_category_list)));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CATEGORY,Arrays
                .asList(getResources().getStringArray(R.array.lost_category_list)).get(mCurrentPosition));
        fragmentList.get(mCurrentPosition).setArguments(bundle);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragmentList.size());
        tab.setupWithViewPager(pager);*/
        pager.setAdapter(mAdapter);
        pager.setOffscreenPageLimit(9);
        tab.setupWithViewPager(pager);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void changeViewPagerIndex(int index) {
        pager.setCurrentItem(index);
    }

}
