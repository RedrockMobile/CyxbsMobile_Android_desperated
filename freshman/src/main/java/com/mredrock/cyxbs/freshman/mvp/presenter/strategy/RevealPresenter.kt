package com.mredrock.cyxbs.freshman.mvp.presenter.strategy

import android.content.Context
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.RevealContract.*
import com.mredrock.cyxbs.freshman.mvp.model.strategy.RevealModel
import com.mredrock.cyxbs.freshman.ui.activity.campus.CampusDataDetailActivity
import com.mredrock.cyxbs.freshman.utils.kt.BasePresenter

class RevealPresenter : BasePresenter<IRevealView, IRevealModel>(), IRevealPresenter {
    override fun onRefresh() =
            model.getAcademyName(mvpView!!::onGetAcademyName)


    override fun onShowDetail(name: String, context: Context) = CampusDataDetailActivity.start(name, context)

    override fun createModel(): IRevealModel = RevealModel()

}