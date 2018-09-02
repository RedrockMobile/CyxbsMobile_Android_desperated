package com.mredrock.cyxbs.freshman.ui.activity.strategy

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.RevealContract.IRevealPresenter
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.RevealContract.IRevealView
import com.mredrock.cyxbs.freshman.ui.adapter.RevealAdapter
import com.mredrock.cyxbs.freshman.utils.kt.BaseMVPActivity
import com.mredrock.cyxbs.freshman.utils.net.Const
import kotlinx.android.synthetic.main.freshman_activity_strategy_same.*

class RevealMVPActivity : BaseMVPActivity<IRevealView, IRevealPresenter>() {
    override fun getLayoutResID() = R.layout.freshman_activity_strategy_same

    override fun getToolbarTitle() = Const.INDEX_REVEAL

    val adapter = RevealAdapter()

    override fun createPersenter(): IRevealPresenter = adapter.presenter
    override fun getViewAttachPersenter(): IRevealView = adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rv_strategy.layoutManager = LinearLayoutManager(this)
        rv_strategy.adapter = adapter

        adapter.srl = srl_refresh

    }

}
