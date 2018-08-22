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

    var isCanHind = true

    val listener = object : FloatingActionButton.OnVisibilityChangedListener() {
        override fun onHidden(fab: FloatingActionButton?) {
            super.onHidden(fab)
            fab?.visibility = View.INVISIBLE
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int) = isScrollVertical(axes)

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        if (isCanHind) {
            child.hide(listener)
            isCanHind = false
        }
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, type: Int) {
        child.show()
        isCanHind = true
    }

    private fun isScrollVertical(axes: Int) = axes == ViewCompat.SCROLL_AXIS_VERTICAL

}