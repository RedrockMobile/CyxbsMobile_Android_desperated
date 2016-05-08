package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.network.exception.RedrockApiException;

import rx.functions.Func1;

/**
 * Created by cc on 16/5/6.
 */
public class RedrockApiWrapperFunc<T> implements Func1<RedrockApiWrapper<T>, T> {

    @Override
    public T call(RedrockApiWrapper<T> wrapper) {
        if (wrapper.status != Const.REDROCK_API_STATUS_SUCCESS) {
            throw new RedrockApiException(wrapper.info);
        }
        return wrapper.data;
    }
}
