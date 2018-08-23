package com.mredrock.cyxbs.freshman.ui.adapter.strategy

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.bean.StrategyData
import com.mredrock.cyxbs.freshman.ui.activity.start
import com.mredrock.cyxbs.freshman.utils.kt.getScreenWidth
import kotlinx.android.synthetic.main.freshman_item_strategy_bank.view.*

class BankAdapter(val list: List<StrategyData.DetailData>) : RecyclerView.Adapter<BankAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (scale6 == 0f) {
            g = Glide.with(parent.context)
            scale6 = getScreenWidth() * 6 / 375f
            scale18 = (scale6 * 3).toInt()
            scale36 = (scale6 * 6).toInt()
            scale89 = (scale6 * 89 / 6.0).toInt()
            scale102 = (scale6 * 17).toInt()
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.freshman_item_strategy_bank, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.v.initView(list[position])

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    private lateinit var g: RequestManager
    private var scale6 = 0f
    private var scale18 = 0
    private var scale36 = 0
    private var scale89 = 0
    private var scale102 = 0

    private fun View.initView(mData: StrategyData.DetailData) {
        tv_name.text = mData.name
        tv_name.layoutParams.height = scale18

        tv_detail.text = mData.content
        tv_detail.setLineSpacing(scale6, 1f)
        tv_detail.layoutParams.height = scale36

        rl.layoutParams.height = scale89
        iv_img.apply {
            iv_img.layoutParams.width = scale102
            setOnClickListener { start(context, mData.picture) }
            g.load(mData.picture.first()).into(this)
        }
    }
}