package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.R
import com.contest.chart.utils.getColor

abstract class BaseScale<T>(resources: Resources, private val provider: ChartDetailsProvider) {
    protected val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.scaleText)
        textSize = 23f
    }

    abstract fun setData(data: T)
    abstract fun onFocusedRangeChanged(focusedRange: IntRange)
    abstract fun setSize(viewWidth: Int, viewHeight: Int)
    abstract fun draw(canvas: Canvas)
    abstract fun onLineStateChanged()

    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        paintText.color = resources.getColor(R.color.scaleText, R.color.scaleTextNight, nightMode)
    }

    fun getStepY(): Float {
        return provider.getChartStep().yStep
    }

    fun getStepX(): Float {
        return provider.getChartStep().xStep
    }
}