package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.network.exception.RedrockApiException;

import io.reactivex.functions.Function;


/**
 * Created by cc on 16/5/6.
 */
public class RedrockApiWrapperFunc<T> implements Function<RedrockApiWrapper<T>, T> {

    @Override
    public T apply(RedrockApiWrapper<T> wrapper) throws Exception {
        if (wrapper.status != Const.REDROCK_API_STATUS_SUCCESS) {
            throw new RedrockApiException(wrapper.info);
        }
        return wrapper.data;
    }
}
