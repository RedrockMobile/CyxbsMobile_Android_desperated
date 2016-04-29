package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.fragment.RestaurantDetailFragment;
import com.mredrock.cyxbsmobile.util.ActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestaurantDetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        initToolbar();

        RestaurantDetailFragment fragment = (RestaurantDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.restaurant_detail_contentFrame);
        if (fragment == null) {
            fragment = RestaurantDetailFragment.newInstance(id);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.restaurant_detail_contentFrame);
        }

//        mRestaurantPhone.setOnClickListener(view -> new MaterialDialog.Builder(AroundEatDetilActivity.this)
//                .title("拨打电话")
//                .titleColor(AroundEatDetilActivity.this.getResources().getColor(R.color.dialog_blue))
//                .backgroundColor(AroundEatDetilActivity.this.getResources().getColor(R.color.white))
//                .positiveColor(AroundEatDetilActivity.this.getResources().getColor(R.color.dialog_blue))
//                .negativeColor(AroundEatDetilActivity.this.getResources().getColor(R.color.dialog_nav))
//                .content("是否拨打该电话？")
//                .theme(Theme.LIGHT)
//                .positiveText("好的")
//                .negativeText("不了")
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + detil.data.shop_tel));
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                return;
//                            }
//                        }
//                        startActivity(intent1);
//                    }
//                }).show());
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
    }

}
