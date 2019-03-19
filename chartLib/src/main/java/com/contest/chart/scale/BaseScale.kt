package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import com.contest.chart.R
import com.contest.chart.utils.getColor

abstract class BaseScale<T>(resources: Resources) {
    protected val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.scaleText)
        textSize = 23f
    }

    abstract fun setData(data: T)
    abstract fun onFocusedRangeChanged(focusedRange: IntRange)
    abstract fun setSize(viewWidth: Int, viewHeight: Int)
    abstract fun draw(canvas: Canvas)
    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        paintText.color = resources.getColor(R.color.scaleText, R.color.scaleTextNight, nightMode)
    }
}