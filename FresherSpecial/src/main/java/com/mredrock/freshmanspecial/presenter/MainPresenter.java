package com.mredrock.freshmanspecial.presenter;

import android.content.Context;
import android.content.Intent;

import com.mredrock.freshmanspecial.guidelines.Guidelines;
import com.mredrock.freshmanspecial.view.DataActivity;
import com.mredrock.freshmanspecial.view.JunxunActivity;
import com.mredrock.freshmanspecial.view.SpecialMainActivity;
import com.mredrock.freshmanspecial.view.MienActivity;

/**
 * Created by zia on 17-8-3.
 */

public class MainPresenter implements IMainPresenter {

    private Context context;

    public MainPresenter(SpecialMainActivity activity){
        context = activity;
    }

    @Override
    public void gotoJunxunActivity() {
        Intent intent = new Intent(context, JunxunActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void gotoDataActivity() {
        Intent intent = new Intent(context, DataActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void gotoMienActivity() {
        Intent intent = new Intent(context, MienActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void gotoStrategyActivity() {
        Intent intent = new Intent(context, Guidelines.class);
        context.startActivity(intent);
    }
}
