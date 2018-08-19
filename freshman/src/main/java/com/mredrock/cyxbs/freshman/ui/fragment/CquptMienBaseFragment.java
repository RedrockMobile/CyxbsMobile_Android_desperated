package com.mredrock.cyxbs.freshman.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.mvp.contract.CquptMienBaseContract;
import com.mredrock.cyxbs.freshman.mvp.model.CquptMienBaseModel;
import com.mredrock.cyxbs.freshman.mvp.presenter.CquptMienBasePresenter;
import com.mredrock.cyxbs.freshman.ui.adapter.MyFragmentPagerAdapter;
import com.mredrock.cyxbs.freshman.utils.DensityUtils;

import java.util.List;

/**
 * 重邮风采第一个页面 嵌套viewpager展示学生组织
 */
@SuppressLint("ValidFragment")
public class CquptMienBaseFragment extends Fragment implements CquptMienBaseContract.ICquptMienBaseView {
    private View parent;
    private CquptMienBasePresenter presenter;
    private ViewPager pager;
    private TabLayout layout;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.freshman_fragment_cqupt_mien_stu, container, false);
        findById();
        initMvp();
        return parent;
    }

    private void findById() {
        pager = parent.findViewById(R.id.freshman_CyMien_stu_vp);
        layout = parent.findViewById(R.id.freshman_CyMien_stu_tl);
    }

    private void initMvp() {
        presenter = new CquptMienBasePresenter(new CquptMienBaseModel());
        presenter.attachView(this);
        presenter.start();
    }

    @Override
    public void setData(List<Fragment> list, List<String> titles) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(((AppCompatActivity) context).getSupportFragmentManager(), list, titles);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(9);
        layout.setupWithViewPager(pager);

        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.height = DensityUtils.getScreenHeight(getActivity()) / 16;
        layout.setLayoutParams(layoutParams);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
