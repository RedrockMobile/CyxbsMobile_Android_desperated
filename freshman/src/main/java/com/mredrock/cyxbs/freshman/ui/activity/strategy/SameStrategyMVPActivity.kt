package com.mredrock.cyxbs.freshman.ui.activity.strategy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.bean.StrategyData
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.SameStrategyContract.ISameStrategyPresenter
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.SameStrategyContract.ISameStrategyView
import com.mredrock.cyxbs.freshman.mvp.presenter.strategy.SameStrategyPresenter
import com.mredrock.cyxbs.freshman.ui.adapter.strategy.BankAdapter
import com.mredrock.cyxbs.freshman.ui.adapter.strategy.StrategyAdapter
import com.mredrock.cyxbs.freshman.utils.kt.BaseMVPActivity
import com.mredrock.cyxbs.freshman.utils.net.Const.*
import kotlinx.android.synthetic.main.freshman_activity_strategy_same.*

fun createStrategyActivity(context: Context, label: String) {
    context.startActivity(Intent(context,
            when (label) {
                INDEX_DORMITORY -> DormitoryStrategyActivity::class.java
                INDEX_REVEAL -> RevealMVPActivity::class.java
                else -> SameStrategyMVPActivity::class.java
            }).apply {
        putExtra("label", label)
    })
}

class SameStrategyMVPActivity : BaseMVPActivity<ISameStrategyView, ISameStrategyPresenter>(), ISameStrategyView {
    override fun getLayoutResID() = R.layout.freshman_activity_strategy_same

    override fun getToolbarTitle() = intent.getStringExtra("label")

    override fun refreshView(mData: StrategyData) {
        rv_strategy.adapter =
                when (intent.getStringExtra("label")) {
                    INDEX_BANK, INDEX_EXPRESS -> BankAdapter(mData.details)
                    else -> StrategyAdapter(mData.details)
                }
        srl_refresh.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val label = intent.getStringExtra("label")

        rv_strategy.layoutManager = LinearLayoutManager(this)

        if (label == INDEX_CATE) {
            best_cate_ever.visibility = View.VISIBLE
        }

        persenter.refreshData(label)
        srl_refresh.setOnRefreshListener { persenter.refreshData(label) }
    }


    override fun getViewAttachPersenter() = this
    override fun createPersenter() = SameStrategyPresenter()
}
