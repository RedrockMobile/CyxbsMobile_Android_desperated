package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.UpdateInfo;

import io.reactivex.functions.Function;


/**
 * Created by cc on 16/5/8.
 */
public class UpdateVerifyFunc implements Function<UpdateInfo, UpdateInfo> {

    private int versionCode;

    public UpdateVerifyFunc(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public UpdateInfo apply(UpdateInfo updateInfo) throws Exception {
        if (updateInfo.versionCode <= versionCode) {
            return null;
        }
        return updateInfo;
    }
}
