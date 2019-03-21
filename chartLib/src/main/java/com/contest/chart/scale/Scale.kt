package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.model.LineChartData

class Scale(resources: Resources, provider: ChartDetailsProvider) : BaseScale<LineChartData>(resources, provider) {

    private val horizontalScale = HorizontalScale(resources, provider)
    private val verticalScale = VerticalScale(resources, provider)
    override fun setData(data: LineChartData) {
        horizontalScale.setData(data.timeLine)
        verticalScale.setData(data.brokenLines)
    }

    override fun setSize(viewWidth: Int, viewHeight: Int) {
        horizontalScale.setSize(viewWidth, viewHeight)
        verticalScale.setSize(viewWidth, viewHeight)
    }

    override fun draw(canvas: Canvas) {
        horizontalScale.draw(canvas)
        verticalScale.draw(canvas)
    }

    override fun onFocusedRangeChanged(focusedRange: IntRange) {
        horizontalScale.onFocusedRangeChanged(focusedRange)
        verticalScale.onFocusedRangeChanged(focusedRange)
    }
}