package com.mredrock.cyxbs.ui.fragment.lost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.lost.LostActivity;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;

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

    @Bind(R.id.rv_content) RecyclerView recycler;

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
        return inflater.inflate(R.layout.fragment_lost, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
