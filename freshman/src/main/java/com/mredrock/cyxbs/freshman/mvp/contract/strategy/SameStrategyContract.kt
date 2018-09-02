package com.mredrock.cyxbs.freshman.mvp.contract.strategy

import com.mredrock.cyxbs.freshman.bean.StrategyData
import com.mredrock.cyxbs.freshman.mvp.presenter.IBasePresenter

class SameStrategyContract {
    interface ISameStrategyModel {
        fun getStrategyData(label: String, onSuccess: (StrategyData) -> Unit, onFail: (Throwable) -> Unit = { it.printStackTrace() })
    }

    interface ISameStrategyView {
        fun refreshView(mData: StrategyData)
    }

    interface ISameStrategyPresenter : IBasePresenter<ISameStrategyView> {
        fun refreshData(label: String)
    }
}