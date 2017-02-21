package com.mredrock.cyxbs.ui.activity.lost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.lost.Lost;
import com.mredrock.cyxbs.model.lost.LostDetail;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;

import butterknife.Bind;

public class LostDetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mTitle;
    @Bind(R.id.detail_lost_img_avatar)
    CircularImageView mAvatar;
    @Bind(R.id.detail_lost_text_nickname)
    TextView mNickName;
    @Bind(R.id.detail_lost_text_kind)
    TextView mType;
    @Bind(R.id.textView_content)
    TextView mContent;
    @Bind(R.id.lost_detail_connect)
    TextView mConnectName;
    @Bind(R.id.lost_detail_time)
    TextView mTime;
    @Bind(R.id.lost_detail_place)
    TextView mPlace;
    @Bind(R.id.lost_detail_tel)
    TextView mTel;
    @Bind(R.id.lost_detail_qq)
    TextView mQQ;
    Lost lost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_details);
        mTitle.setText("详细信息");
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
    public void init(){
        lost = (Lost) getIntent().getSerializableExtra("LOST");
        if (lost != null){
            RequestManager.getInstance().getLostDetail(new SimpleSubscriber<LostDetail>(this, new SubscriberListener<LostDetail>() {
                @Override
                public boolean onError(Throwable e) {
                    Toast.makeText(LostDetailsActivity.this, "抱歉，未拉取到数据", Toast.LENGTH_SHORT).show();
                    return super.onError(e);
                }

                @Override
                public void onNext(LostDetail lostDetail) {
                    super.onNext(lostDetail);
                    showDetail(lostDetail);
                }
            }),lost);
        }else {
            Toast.makeText(this, "抱歉，未拉取到数据", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDetail(LostDetail l){
        mNickName.setText(APP.getUser(LostDetailsActivity.this).getNickname());
        Glide.with(LostDetailsActivity.this).load(l.avatar).into(mAvatar);
        mType.setText(l.category);
        mConnectName.setText(l.connectName);
        mPlace.setText(l.place);
        mContent.setText(l.description);
        mTel.setText(l.connectPhone);
        mQQ.setText(l.connectWx);
        mTime.setText(l.time);
    }
}
