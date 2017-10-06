package com.mredrock.cyxbs.ui.fragment.me;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;
import com.mredrock.cyxbs.ui.adapter.me.VolunteerRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glossimar on 2017/10/3.
 */

public class NoTimeVolunteerFragment extends Fragment{
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_volunteer_notime,container,false);
        return view;
    }

}
