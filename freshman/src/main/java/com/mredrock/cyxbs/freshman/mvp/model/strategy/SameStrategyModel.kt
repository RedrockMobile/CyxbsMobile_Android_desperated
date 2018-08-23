package com.mredrock.cyxbs.freshman.mvp.model.strategy

import com.mredrock.cyxbs.freshman.bean.StrategyData
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.SameStrategyContract
import com.mredrock.cyxbs.freshman.utils.kt.withSPCache
import com.mredrock.cyxbs.freshman.utils.net.Const.*

class SameStrategyModel : SameStrategyContract.ISameStrategyModel {
    override fun getStrategyData(label: String, onSuccess: (StrategyData) -> Unit, onFail: (Throwable) -> Unit) =
            withSPCache(label, StrategyData::class.java, {
                when (label) {
                    DORMITORY_NAME_1, DORMITORY_NAME_2, DORMITORY_NAME_3, DORMITORY_NAME_4 -> this.getDormitoryData(label)
                    else -> this.getStrategyData(label, STRATEGY_PAGE_NUM, STRATEGY_PAGE_SIZE)
                }
            }, {
                it.details.forEach { detail ->
                    detail.picture = detail.picture.map { url ->
                        IMG_BASE_URL + url
                    }
                }

                onSuccess(it)
            }, onFail, "strategy")
}