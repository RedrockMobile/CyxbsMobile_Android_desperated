package com.mredrock.cyxbs.freshman.ui.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BasePagerAdapter<V : View, D>(var list: List<D> = listOf()) : PagerAdapter() {

    fun refresh(list: List<D>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getCount() = list.size

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = container.removeView(`object` as View)

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    @Suppress("UNCHECKED_CAST")
    override fun instantiateItem(container: ViewGroup, position: Int): V =
            (getLayoutId()?.let { LayoutInflater.from(container.context).inflate(it, container, false) as? V }
                    ?: createView(container.context)
                    ?: throw Exception("Can't create View,Please override getLayoutId() or createView()"))
                    .apply {
                        initView(list[position])
                        container.addView(this)
                    }


    protected open fun getLayoutId(): Int? = null

    protected open fun createView(context: Context): V? = null

    abstract fun V.initView(mData: D)

}