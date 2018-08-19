package com.mredrock.cyxbs.freshman.ui.adapter

import com.mredrock.cyxbs.freshman.bean.MienStu
import com.mredrock.cyxbs.freshman.bean.StrategyData
import com.mredrock.cyxbs.freshman.ui.adapter.strategy.StrategyAdapter

class CquptAdapter(list: List<MienStu.ArrayBean>) : StrategyAdapter(list.map {
    StrategyData.DetailData().apply {
        id = it.id
        name = it.name
        content = it.content
        picture = it.picture
    }
})