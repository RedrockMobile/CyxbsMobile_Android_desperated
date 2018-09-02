package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.network.exception.RedrockApiException;

import io.reactivex.functions.Function;
import kotlin.Unit;

/**
 * Created By jay68 on 2018/3/1.
 */

public class RedrockApiNoDataWrapperFunc implements Function<RedrockApiWrapper<Unit>, Unit> {

    @Override
    public Unit apply(RedrockApiWrapper<Unit> wrapper) throws Exception {
        if (wrapper.status != Const.REDROCK_API_STATUS_SUCCESS) {
            throw new RedrockApiException(wrapper.info);
        }
        return Unit.INSTANCE;
    }
}
