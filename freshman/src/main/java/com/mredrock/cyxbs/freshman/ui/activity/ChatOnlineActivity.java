package com.mredrock.cyxbs.freshman.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.ui.adapter.MyFragmentPagerAdapter;
import com.mredrock.cyxbs.freshman.ui.fragment.ChatOnlineFragment;
import com.mredrock.cyxbs.freshman.utils.TabLayoutUtil;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 线上交流
 */
public class ChatOnlineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = findViewById(R.id.freshman_chatOnline_vp);
        TabLayout tabLayout = findViewById(R.id.freshman_chatOnline_tl);
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        ChatOnlineFragment fragment = new ChatOnlineFragment();
        fragment.setInit("学校群");
        ChatOnlineFragment fragment1 = new ChatOnlineFragment();
        fragment1.setInit("老乡群");

        fragments.add(fragment);
        fragments.add(fragment1);
        titles.add("学校群");
        titles.add("老乡群");
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(viewPager.getChildCount());
        tabLayout.setupWithViewPager(viewPager);
        TabLayoutUtil.setIndicator(tabLayout, 40, 40);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(fragment.getEditText().getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(fragment1.getEditText().getWindowToken(), 0);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public int getLayoutResID() {
        return R.layout.freshman_activity_chat_online;
    }

    @NotNull
    @Override
    public String getToolbarTitle() {
        return Const.INDEX_CHAT;
    }
}
