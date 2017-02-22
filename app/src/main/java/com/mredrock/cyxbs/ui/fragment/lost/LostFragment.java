package com.mredrock.cyxbs.ui.fragment.lost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.lost.LostActivity;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.util.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostFragment extends BaseFragment {

    public static final String ARGUMENT_THEME = "theme";
    public static final String ARGUMENT_CATEGORY = "category";

    int theme = LostActivity.THEME_LOST;
    String category;

    @Bind(R.id.information_RecyclerView) RecyclerView recycler;
    @Bind(R.id.fab_main) FloatingActionButton mFabMain;
    @Bind(R.id.information_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments;
        if ((arguments = getArguments()) != null) {
            theme = arguments.getInt(ARGUMENT_THEME);
            category = arguments.getString(ARGUMENT_CATEGORY);
        } else {
            category = getResources().getStringArray(R.array.lost_category_list)[0];
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "show me your floating ", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
