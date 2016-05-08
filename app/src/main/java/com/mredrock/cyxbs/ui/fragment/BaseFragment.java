package com.mredrock.cyxbs.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {
    private View rootView;

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rootView = view;
        super.onViewCreated(view, savedInstanceState);
    }

}
