package com.mredrock.cyxbs.freshman.ui.adapter.strategy

import android.content.Context
import android.os.Build
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.makeramen.roundedimageview.RoundedImageView
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.bean.StrategyData
import com.mredrock.cyxbs.freshman.ui.activity.start
import com.mredrock.cyxbs.freshman.ui.adapter.BasePagerAdapter
import com.mredrock.cyxbs.freshman.utils.DensityUtils.dp2px
import com.mredrock.cyxbs.freshman.utils.kt.getScreenHeight
import kotlinx.android.synthetic.main.freshman_item_strategy.view.*

private val dp4 = dp2px(4f).toInt()
private val dp6 = dp2px(6f)
private val dp8 = dp4 * 2
private val imgVPHeight = (getScreenHeight() * 164 / 667.0).toInt()

open class StrategyAdapter(private val list: List<StrategyData.DetailData>) : RecyclerView.Adapter<StrategyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.freshman_item_strategy, parent, false)
        if (lParams == null) {
            g = Glide.with(parent.context)
            lParams = LinearLayout.LayoutParams(dp8, dp8)
            lParams!!.setMargins(dp4, dp4, dp4, dp4)
        }
        if (pointWidth == 0) {
            v.apply {
                ll_point.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ll_point.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                        if (ll_point.childCount < 2) {
                            return
                        }
                        pointWidth = ll_point.getChildAt(0).left - ll_point.getChildAt(1).left
                    }
                })
            }
        }
        v.vp_strategy.layoutParams.height = imgVPHeight

        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: StrategyAdapter.ViewHolder, position: Int) = holder.v.initView(list[position], position)

    private var pointWidth = 0
    private var lParams: LinearLayout.LayoutParams? = null
    private lateinit var g: RequestManager

    private fun View.initView(mData: StrategyData.DetailData, pos: Int) {
        tv_describe_name.text = mData.name
        tv_describe_detail.text = mData.content
        if (!mData.property.isNullOrBlank()) {
            tv_tag.visibility = View.VISIBLE
            tv_describe_money_per.visibility = View.VISIBLE
            tv_tag.text = (pos + 1).toString()
            tv_describe_money_per.text = "￥${mData.property}（人）"
        }

        if (mData.picture.size < 2) {
            iv_index_point.visibility = View.GONE
        } else {
            with(ll_point) {
                for (i in childCount until mData.picture.size) {
                    addView(ImageView(context).apply {
                        layoutParams = lParams
                        setImageResource(R.drawable.freshman_ic_point_gray)
                    })
                }
            }
            vp_strategy.addOnPageChangeListener(IndexPageListener(iv_index_point))
        }
        vp_strategy.adapter = ImagePageAdapter(mData.picture)

    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    inner class IndexPageListener(private val indexView: ImageView) : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            indexView.translationX = -(position + positionOffset) * pointWidth
        }

        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageSelected(position: Int) {}
    }

    private inner class ImagePageAdapter(private val picUrls: List<String>) : BasePagerAdapter<RoundedImageView, String>(picUrls) {
        override fun createView(context: Context) = RoundedImageView(context)
        override fun RoundedImageView.initView(mData: String) {
            adjustViewBounds = true
            cornerRadius = dp6
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener { _ ->
                start(context, picUrls, picUrls.indexOf(mData))
            }
            g.load(mData).thumbnail(0.1f).into(this)
        }
    }
}