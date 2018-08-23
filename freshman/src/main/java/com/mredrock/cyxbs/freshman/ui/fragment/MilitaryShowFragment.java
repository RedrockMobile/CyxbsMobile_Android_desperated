package com.mredrock.cyxbs.freshman.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.MilitaryShow;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryShowContract;
import com.mredrock.cyxbs.freshman.mvp.model.MilitaryShowModel;
import com.mredrock.cyxbs.freshman.mvp.presenter.MilitaryShowPresenter;
import com.mredrock.cyxbs.freshman.ui.adapter.ViewPagerPhotoCardAdapter;
import com.mredrock.cyxbs.freshman.ui.adapter.ViewPagerVideoAdapter;
import com.mredrock.cyxbs.freshman.utils.DensityUtils;
import com.mredrock.cyxbs.freshman.utils.banner.CardTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 军训风采展示的fragment
 */
public class MilitaryShowFragment extends Fragment implements MilitaryShowContract.IMilitaryShowView {
    private View parent;

    private MilitaryShowPresenter presenter;
    private ViewPager viewPager;
    private ViewPager videoPager;
    public List<String> photos;
    private LinearLayout parent_ll2;
    private LinearLayout parent_ll;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.freshman_fragment_military_show, container, false);
        findById();
        initMvp();
        addListener();
        return parent;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void findById() {
        videoPager = parent.findViewById(R.id.freshman_military_show_video_vp);
        viewPager = parent.findViewById(R.id.freshman_military_show_photo_vp);
        parent_ll2 = parent.findViewById(R.id.freshman_military_show_video_parent2);
        parent_ll = parent.findViewById(R.id.freshman_military_show_video_parent1);
    }

    private void initMvp() {
        presenter = new MilitaryShowPresenter(new MilitaryShowModel());
        presenter.attachView(this);
        presenter.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener() {
        parent_ll.setOnTouchListener((v, event) -> {
            parent_ll.getParent().requestDisallowInterceptTouchEvent(true);
            return viewPager.dispatchTouchEvent(event);
        });
        parent_ll2.setOnTouchListener((v, event) -> {
            parent_ll2.getParent().requestDisallowInterceptTouchEvent(true);
            return videoPager.dispatchTouchEvent(event);
        });
    }


    @Override
    public void setData(MilitaryShow bean) {
        photos = new ArrayList<>();
        for (MilitaryShow.PictureBean bean1 : bean.getPicture()) {
            photos.add(bean1.getUrl());
        }

        videoPager.setAdapter(new ViewPagerVideoAdapter(getContext(), bean.getVideo()));
        videoPager.setOffscreenPageLimit(bean.getVideo().size());
        videoPager.setPageMargin((DensityUtils.getScreenWidth(getActivity()) / 8));
        videoPager.setPageTransformer(true, new CardTransformer());

        viewPager.setAdapter(new ViewPagerPhotoCardAdapter(getContext(), bean.getPicture(), photos));
        viewPager.setPageMargin(DensityUtils.getScreenWidth(getActivity()) / 10);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageTransformer(true, new CardTransformer());

        viewPager.setCurrentItem(bean.getPicture().size() * 1000);


    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}

