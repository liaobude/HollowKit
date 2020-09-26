package com.funnywolf.hollowkit.view.scroll.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.ViewCompat

/**
 * 弹性布局
 *
 * @author https://github.com/funnywolfdadada
 * @since 2020/9/28
 */
class JellyLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BehavioralScrollView(context, attrs, defStyleAttr), NestedScrollBehavior {

    override var behavior: NestedScrollBehavior? = this

    @ViewCompat.ScrollAxis
    var scrollAxis = ViewCompat.SCROLL_AXIS_NONE

    /**
     * 滚动阻尼，参数为当前的滚动量，返回值未阻尼系数
     */
    var resistance: ((JellyLayout, Int)->Float)? = null

    /**
     * 手指抬起时的回调
     */
    var onTouchRelease: ((JellyLayout)->Unit)? = null

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount == 1) {
            midView = getChildAt(0)
        } else {
            prevView = getChildAt(0)
            midView = getChildAt(1)
            nextView = getChildAt(2)
        }
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun scrollAxis(): Int = scrollAxis

    private fun selfScrolled() = when (nestedScrollAxes) {
        ViewCompat.SCROLL_AXIS_VERTICAL -> scrollY != 0
        ViewCompat.SCROLL_AXIS_HORIZONTAL -> scrollX != 0
        else -> false
    }

    override fun handleDispatchTouchEvent(e: MotionEvent): Boolean? {
        if ((e.action == MotionEvent.ACTION_CANCEL || e.action == MotionEvent.ACTION_UP) && selfScrolled()) {
            onTouchRelease?.invoke(this) ?: smoothScrollTo(0)
            return true
        }
        return super.handleDispatchTouchEvent(e)
    }

    override fun handleNestedPreScrollFirst(scroll: Int, type: Int): Boolean? {
        return if (selfScrolled()) {
            false
        } else {
            null
        }
    }

    override fun handleNestedScrollFirst(scroll: Int, type: Int): Boolean? {
        return false
    }

    override fun handleScrollSelf(scroll: Int, type: Int): Boolean? {
        return when (type) {
            ViewCompat.TYPE_NON_TOUCH -> false
            ViewCompat.TYPE_TOUCH -> {
                val s = (scroll * (resistance?.invoke(this, scroll) ?: 1F)).toInt()
                when (nestedScrollAxes) {
                    ViewCompat.SCROLL_AXIS_VERTICAL -> scrollBy(0, s)
                    ViewCompat.SCROLL_AXIS_HORIZONTAL -> scrollBy(s, 0)
                }
                true
            }
            else -> null
        }
    }

}
