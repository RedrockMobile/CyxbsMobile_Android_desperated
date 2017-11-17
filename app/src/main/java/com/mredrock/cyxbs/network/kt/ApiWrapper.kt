package com.mredrock.cyxbs.network.kt

import com.mredrock.cyxbs.config.Const
import com.mredrock.cyxbs.model.RedrockApiWrapper
import com.mredrock.cyxbs.network.exception.RedrockApiException
import io.reactivex.functions.Function

/**
 * Created by mingyang.zeng on 2017/8/29.
 */
class ApiWrapperFunc<T> : Function<RedrockApiWrapper<T>, T> {

    override fun apply(wrapper: RedrockApiWrapper<T>): T {
        if (wrapper.status != Const.REDROCK_API_STATUS_SUCCESS) {
            throw RedrockApiException(wrapper.info)
        }
        return wrapper.data
    }
}