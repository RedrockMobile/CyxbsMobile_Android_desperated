package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.UpdateInfo;

import rx.functions.Func1;

/**
 * Created by cc on 16/5/8.
 */
public class UpdateVerifyFunc implements Func1<UpdateInfo, UpdateInfo> {

    private int versionCode;

    public UpdateVerifyFunc(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public UpdateInfo call(UpdateInfo updateInfo) {
        if (updateInfo.versionCode <= versionCode) {
            return null;
        }
        return updateInfo;
    }
}
