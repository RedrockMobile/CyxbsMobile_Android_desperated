package com.mredrock.cyxbs.ui.activity.lost;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.lost.LostDetail;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LostDetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mTitle;
    @BindView(R.id.detail_lost_img_avatar)
    CircularImageView mAvatar;
    @BindView(R.id.detail_lost_text_nickname)
    TextView mNickName;
    @BindView(R.id.detail_lost_text_kind)
    TextView mType;
    @BindView(R.id.textView_content)
    TextView mContent;
    @BindView(R.id.lost_detail_connect)
    TextView mConnectName;
    @BindView(R.id.lost_detail_time)
    TextView mTime;
    @BindView(R.id.lost_detail_place)
    TextView mPlace;
    @BindView(R.id.lost_detail_tel)
    TextView mTel;
    @BindView(R.id.lost_detail_qq)
    TextView mQQ;
    LostDetail lostDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_details);
        ButterKnife.bind(this);
        mTitle.setText("详细信息");
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init();
    }

    public void init() {
        /*lost = (Lost) getIntent().getSerializableExtra("LOST");
        if (lost != null){
            RequestManager.getInstance().getLostDetail(new SimpleObserver<LostDetail>(this, new SubscriberListener<LostDetail>() {
                @Override
                public boolean onError(Throwable e) {
                    Toast.makeText(LostDetailsActivity.this, "抱歉，未拉取到数据", Toast.LENGTH_SHORT).show();
                    return super.onError(e);
                }

                @Override
                public void onNext(LostDetail lostDetail) {
                    super.onNext(lostDetail);
                }
            }),lost);
        }else {
            Toast.makeText(this, "抱歉，未拉取到数据", Toast.LENGTH_SHORT).show();
        }*/
        lostDetail = (LostDetail) getIntent().getSerializableExtra("LostDetail");
        mNickName.setText(lostDetail.connectName);
        ImageLoader.getInstance().loadAvatar(lostDetail.avatar, mAvatar);
        mType.setText(lostDetail.category);
        mConnectName.setText(lostDetail.connectName);
        mPlace.setText(lostDetail.place);
        mContent.setText(lostDetail.description);
        mTel.setText(lostDetail.connectPhone);
        mQQ.setText(lostDetail.connectWx);
        mTime.setText(lostDetail.time);
    }


}
