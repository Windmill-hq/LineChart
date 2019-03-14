package com.contest.chart.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

abstract class MeasuredView : View {

    private var totalWidth = 0
    private var totalHight = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val w = measureDimension(desiredWidth, widthMeasureSpec)
        val h = measureDimension(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(w, h)
        totalWidth = w
        totalHight = h
        onMeasured(w, h)
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    abstract fun onMeasured(width: Int, height: Int)
}