package com.mredrock.cyxbs.freshman.utils.kt

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by 某只机智 on 2018/7/17.
 */
class HideFabBehavior : FloatingActionButton.Behavior {
    constructor(c: Context, a: AttributeSet) : super(c, a)
    constructor() : super()

    val listener = object : FloatingActionButton.OnVisibilityChangedListener() {
        override fun onHidden(fab: FloatingActionButton?) {
            super.onHidden(fab)
            fab?.visibility = View.INVISIBLE
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int) = isScrollVertical(axes)

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        if (isSlipDown(dyConsumed, dyUnconsumed)) {
            child.show()
        } else if (isSlipUp(dyConsumed, dyUnconsumed)) {
            // 这里不能用默认的hide函数，
            // 因为默认hide动画完成后会将visibility设为GONE，
            // 那样Behavior就不进行拦截了
            child.hide(listener)
        }
    }

    private fun isScrollVertical(axes: Int) = axes == ViewCompat.SCROLL_AXIS_VERTICAL

    private fun isSlipDown(dyConsumed: Int, dyUnconsumed: Int) = (dyConsumed < 0 && dyUnconsumed == 0) || (dyConsumed == 0 && dyUnconsumed < 0)

    private fun isSlipUp(dyConsumed: Int, dyUnconsumed: Int) = (dyConsumed > 0 && dyUnconsumed == 0) || (dyConsumed == 0 && dyUnconsumed > 0)
}