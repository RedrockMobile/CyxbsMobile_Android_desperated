package com.mredrock.cyxbs.freshman.mvp.model.strategy

import com.mredrock.cyxbs.freshman.bean.Entity
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.RevealContract
import com.mredrock.cyxbs.freshman.utils.kt.withSPCache

class RevealModel : RevealContract.IRevealModel {
    override fun getAcademyName(success: (List<String>) -> Unit) =
            withSPCache("academyName", Entity::class.java, { academyName }, { success(it.name) })
}