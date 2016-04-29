package com.mredrock.cyxbsmobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.CommentView;
import com.mredrock.cyxbsmobile.component.widget.EndlessScrollView;
import com.mredrock.cyxbsmobile.model.RestaurantComment;
import com.mredrock.cyxbsmobile.model.RestaurantDetail;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by Stormouble on 16/4/16.
 */
public class RestaurantDetailFragment extends Fragment {
    private static final String SHOP_KEY = "shop_key";

    @Bind(R.id.restaurant_detail_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.restaurant_detail_scroll_view)
    EndlessScrollView mScrollView;
    @Bind(R.id.restaurant_detail_shop_name)
    TextView mRestaurantName;
    @Bind(R.id.restaurant_detail_shop_img)
    ImageView mRestaurantImage;
    @Bind(R.id.restaurant_detail_recommend)
    TextView mRestaurantRecommend;
    @Bind(R.id.restaurant_detail_shop_phone)
    TextView mRestaurantPhone;
    @Bind(R.id.restaurant_detail_shop_location)
    TextView mRestaurantLocation;
    @Bind(R.id.restaurant_detail_comment_view)
    CommentView mCommentView;

    private FloatingActionButton mFloatingActionButton;

    private int mPage = 1;
    private String mShopKey;
    private boolean mIsNoMoreComments = false;

    List<RestaurantComment> mComments;

    public RestaurantDetailFragment() {
        // Requires empty public constructor
    }

    public static RestaurantDetailFragment newInstance(String shopKey) {
        RestaurantDetailFragment fragment = new RestaurantDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_KEY, shopKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShopKey = getArguments().getString(SHOP_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
        ButterKnife.bind(this, root);
        mFloatingActionButton =
                (FloatingActionButton) getActivity().findViewById(R.id.fab);
        runEnterAnimation();
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFloatingActionButton.setVisibility(View.VISIBLE);
        mFloatingActionButton.setImageResource(R.drawable.ic_add);
        mFloatingActionButton.setOnClickListener(v -> showDialog());

        mComments = new ArrayList<>();

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPage = 1;
            mIsNoMoreComments = false;
            getRestaurantAndComments();
        });

        mScrollView.setOnBottomListener(() -> {
            if (!mIsNoMoreComments) {
                mPage++;
                getRestaurantAndComments();
            }
        });

        getRestaurantAndComments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void getRestaurantAndComments() {
        RequestManager.getInstance().getRestaurantAndComments(new Subscriber<RestaurantDetail>() {

            @Override
            public void onStart() {
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
                mIsNoMoreComments = true;
            }

            @Override
            public void onNext(RestaurantDetail restaurantDetail) {
                mRestaurantName.setText(restaurantDetail.shop_name);
                mRestaurantLocation.setText(restaurantDetail.shop_address);
                mRestaurantPhone.setText(restaurantDetail.shop_tel);
                mRestaurantRecommend.setText(restaurantDetail.shop_sale_content);
                Glide.with(getActivity())
                        .load(restaurantDetail.shop_image[0])
                        .crossFade()
                        .into(mRestaurantImage);

                if (Utils.checkNotNullAndNotEmpty(restaurantDetail.restaurantComments)) {
                    if (mPage == 1) {
                        mComments =restaurantDetail.restaurantComments;
                    } else {
                        mComments.addAll(restaurantDetail.restaurantComments);
                    }
                    mCommentView.setData(mComments);
                }
            }
        }, mShopKey, String.valueOf(mPage));
    }

    private void sendCommentAndRefresh(String content) {
        User user = APP.getUser(getActivity());
        if (user != null) {
            RequestManager.getInstance().sendRestaurantCommentAndRefresh(new Subscriber<List<RestaurantComment>>() {

                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(List<RestaurantComment> data) {
                    if (Utils.checkNotNull(data)) {
                        mCommentView.setData(data);
                    } else {
                        Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT).show();
                    }
                }

            }, mShopKey, user.stuNum, user.idNum, content, user.name);
        }
    }

    private void runEnterAnimation() {
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) mFloatingActionButton.getLayoutParams();
        mFloatingActionButton.setTranslationY(
                mFloatingActionButton.getHeight() + params.bottomMargin);
        mFloatingActionButton.animate()
                .translationY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(700)
                .start();
    }

    private void showDialog() {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_restaurant_detail_dialog, null);
        final EditText edtInput = (EditText) view.findViewById(R.id.input_comment);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.restaurant_detail_dialog_title));
        builder.setView(view);
        builder.setPositiveButton(getResources().getString(R.string.restaurant_detail_dialog_positive_text), (dialog, whichButton) -> {
                    if (!edtInput.getText().toString().isEmpty()) {
                        sendCommentAndRefresh(edtInput.getText().toString());
                    }
                    else {
                        Toast.makeText(getActivity(), "内容不能为空哟", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("取消", (dialog, whichButton) -> {
            dialog.dismiss();
        });
        builder.show();
    }

}
