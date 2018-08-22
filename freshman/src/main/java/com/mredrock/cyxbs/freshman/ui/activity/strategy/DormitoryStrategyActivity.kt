package com.mredrock.cyxbs.freshman.ui.activity.strategy

import android.os.Bundle
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.ui.activity.BaseActivity
import com.mredrock.cyxbs.freshman.ui.adapter.strategy.DormitoryPageAdapter
import kotlinx.android.synthetic.main.freshman_activity_dormitory_strategy.*

class DormitoryStrategyActivity : BaseActivity() {
    override fun getLayoutResID() = R.layout.freshman_activity_dormitory_strategy

    override fun getToolbarTitle(): String = intent.getStringExtra("label")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vp_dormitory.adapter = DormitoryPageAdapter()
        vp_dormitory.offscreenPageLimit = 4
        tab_dormitory.setupWithViewPager(vp_dormitory)
    }
}