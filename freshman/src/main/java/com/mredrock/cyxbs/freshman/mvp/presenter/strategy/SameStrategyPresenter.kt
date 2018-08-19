package com.mredrock.cyxbs.freshman.mvp.presenter.strategy

import com.mredrock.cyxbs.freshman.mvp.contract.strategy.SameStrategyContract.*
import com.mredrock.cyxbs.freshman.mvp.model.strategy.SameStrategyModel
import com.mredrock.cyxbs.freshman.utils.kt.BasePresenter

class SameStrategyPresenter : BasePresenter<ISameStrategyView, ISameStrategyModel>(), ISameStrategyPresenter {
    override fun refreshData(label: String) = model.getStrategyData(label, mvpView!!::refreshView)

    override fun createModel() = SameStrategyModel()

}