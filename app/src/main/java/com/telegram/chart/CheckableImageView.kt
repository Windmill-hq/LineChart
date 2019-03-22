package com.telegram.chart

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.Checkable

class CheckableImageView : AppCompatImageView, Checkable {
    private var mChecked = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }

    override fun toggle() {
        mChecked = mChecked.not()
        refreshDrawableState()
    }

    override fun setChecked(p0: Boolean) {
        mChecked = p0
        refreshDrawableState()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val stateArray = super.onCreateDrawableState(extraSpace + 1)
        if (mChecked) {
            mergeDrawableStates(stateArray, intArrayOf(android.R.attr.state_checked))
        }
        return stateArray
    }
}