package com.contest.chart

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

class LockableScrollView : ScrollView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mScrollable = true

    fun setScrollingEnabled(enabled: Boolean) {
        mScrollable = enabled
    }

    fun isScrollable(): Boolean {
        return mScrollable
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->
                return mScrollable && super.onTouchEvent(ev)
            else -> return super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mScrollable && super.onInterceptTouchEvent(ev)
    }
}