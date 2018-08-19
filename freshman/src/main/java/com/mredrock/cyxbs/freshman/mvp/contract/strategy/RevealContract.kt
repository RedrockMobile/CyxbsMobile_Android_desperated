package com.mredrock.cyxbs.freshman.mvp.contract.strategy

import android.content.Context
import com.mredrock.cyxbs.freshman.mvp.presenter.IBasePresenter

class RevealContract {
    interface IRevealView {
        fun onGetAcademyName(names: List<String>)
    }

    interface IRevealModel {
        fun getAcademyName(success: (List<String>) -> Unit)
    }

    interface IRevealPresenter : IBasePresenter<IRevealView> {
        fun onRefresh()
        fun onShowDetail(name: String, context: Context)
    }
}