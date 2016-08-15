package com.excitingboat.freshmanspecial.view.fragment.Style;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.model.bean.Sight;
import com.excitingboat.freshmanspecial.net.GetInformation;
import com.excitingboat.freshmanspecial.presenter.GetInformationPresenter;
import com.excitingboat.freshmanspecial.view.iview.IGetInformation;

import java.util.List;

/**
 * Created by PinkD on 2016/8/9.
 * BeautifulCQUPTFragment
 */
public class BeautifulCQUPTFragment extends Fragment implements IGetInformation<Sight> {
    public static final boolean DEBUG = false;
    private static final String TAG = "BeautifulCQUPTFragment";
    private GetInformationPresenter<Sight> getInformationPresenter;
    private View view;
    private ImageView[] imageViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreateView: ");
        }
        getInformationPresenter = new GetInformationPresenter<>(this, GetInformation.CQUPT_SIGHT);
        view = inflater.inflate(R.layout.project_freshman_special__fragment_beautiful_cqupt, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageViews = new ImageView[]{
                (ImageView) view.findViewById(R.id.beauty_image_1),
                (ImageView) view.findViewById(R.id.beauty_image_2),
                (ImageView) view.findViewById(R.id.beauty_image_3),
                (ImageView) view.findViewById(R.id.beauty_image_4),
                (ImageView) view.findViewById(R.id.beauty_image_5),
                (ImageView) view.findViewById(R.id.beauty_image_6),
                (ImageView) view.findViewById(R.id.beauty_image_7),
                (ImageView) view.findViewById(R.id.beauty_image_8),
                (ImageView) view.findViewById(R.id.beauty_image_9),
                (ImageView) view.findViewById(R.id.beauty_image_10)
        };
        getInformationPresenter.getInformation(new int[]{0, 15});
    }

    @Override
    public void requestSuccess(List<Sight> list) {
        if (DEBUG) {
            Log.d(TAG, "requestSuccess: " + list);
        }
        for (int i = 0; i < 10; i++) {
            Glide.with(getActivity())
                    .load(list.get(i).getPhoto().get(0).getPhoto_src())
                    .into(imageViews[i]);
        }
    }

    @Override
    public void requestFail() {
        for (int i = 0; i < 10; i++) {
            imageViews[i].setImageResource(R.drawable.picture_load_failed);
        }
        Toast.makeText(getContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getInformationPresenter.unBind();
    }
}
