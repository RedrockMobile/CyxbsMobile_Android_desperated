package com.mredrock.cyxbs.freshman.ui.adapter

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.mvp.contract.strategy.RevealContract
import com.mredrock.cyxbs.freshman.mvp.presenter.strategy.RevealPresenter
import kotlinx.android.synthetic.main.freshman_item_strategy_reveal.view.*

class RevealAdapter : RecyclerView.Adapter<RevealAdapter.ViewHolder>(), RevealContract.IRevealView {
    val presenter: RevealContract.IRevealPresenter = RevealPresenter()
    var list: List<String> = listOf()

    var srl: SwipeRefreshLayout? = null
        set(value) {
            field = value
            field?.setOnRefreshListener(presenter::onRefresh)
            presenter.onRefresh()
        }

    override fun onGetAcademyName(names: List<String>) {
        srl?.isRefreshing = false
        list = names
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.freshman_item_strategy_reveal, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.v.initView(list[position])

    private fun View.initView(name: String) {
        setOnClickListener {
            presenter.onShowDetail(name, context)
        }
        tv_academy_name.text = name
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

}