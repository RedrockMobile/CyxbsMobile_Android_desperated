package com.mredrock.cyxbs.ui.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.R;

/**
 * Created by glossimar on 2017/10/3.
 */

public class NoTimeVolunteerFragment extends Fragment {
    private View view;
    private float currentY;
    private float moveBeginY;
    private float passLengthY;

    private boolean isEnd = true;
    private boolean isRecord = false;

    private RefreshInfoListener infoListener;
    private RefreshListStatusListener listListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_volunteer_notime, container, false);
        return view;
    }

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (isEnd)
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (listListener.checkListStatus() && !isRecord){
//                    isRecord = true;
//                    moveBeginY = motionEvent.getY();
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                    currentY = motionEvent.getY();
//                    passLengthY = currentY - moveBeginY;
//        }
//    }

    public RefreshListStatusListener getListListener() {
        return listListener;
    }

    public void setListListener(RefreshListStatusListener listListener) {
        this.listListener = listListener;
    }

    public RefreshInfoListener getInfoListener() {

        return infoListener;
    }

    public void setInfoListener(RefreshInfoListener infoListener) {
        this.infoListener = infoListener;
    }

    public interface RefreshInfoListener {
        void callRefreshAgain();
    }

    public interface RefreshListStatusListener {
        boolean checkListStatus();
    }
}
