package com.mredrock.cyxbs.freshman.ui.adapter.strategy

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.bean.StrategyData
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.SameStrategyContract.ISameStrategyModel
import com.mredrock.cyxbs.freshman.mvp.model.strategy.SameStrategyModel
import com.mredrock.cyxbs.freshman.ui.adapter.BasePagerAdapter
import com.mredrock.cyxbs.freshman.utils.net.Const.*
import kotlinx.android.synthetic.main.freshman_item_dormitory_page.view.*

class DormitoryPageAdapter : BasePagerAdapter<SwipeRefreshLayout, String>(
        listOf(DORMITORY_NAME_1, DORMITORY_NAME_2, DORMITORY_NAME_3, DORMITORY_NAME_4)) {
    override fun SwipeRefreshLayout.initView(mData: String) {
        rv_strategy.layoutManager = LinearLayoutManager(context)
        val fresh = { it: StrategyData ->
            rv_strategy.adapter = StrategyAdapter(it.details)
            isRefreshing = false
        }
        setOnRefreshListener {
            model.getStrategyData(mData, fresh) { it.printStackTrace() }
        }
        model.getStrategyData(mData, fresh) { it.printStackTrace() }
    }

    private val model: ISameStrategyModel = SameStrategyModel()
    override fun getLayoutId() = R.layout.freshman_item_dormitory_page
    override fun getPageTitle(position: Int) = list[position]
}