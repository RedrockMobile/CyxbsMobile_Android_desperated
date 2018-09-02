package com.mredrock.cyxbs.freshman.mvp.model

import com.mredrock.cyxbs.freshman.mvp.contract.MainContract
import com.mredrock.cyxbs.freshman.utils.kt.defaultSp
import com.mredrock.cyxbs.freshman.utils.kt.invoke

class MainModel : MainContract.IMainModel {
    override fun setNowStop(nowStop: Int) = defaultSp { putInt("lastStop", nowStop) }
    override fun getNowStop() = defaultSp.getInt("lastStop", 0)
}