package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.toStringDate

class HorizontalScale(resources: Resources, provider: ChartDetailsProvider) :
        BaseScale<LineChartData>(resources, provider) {

    private lateinit var timeLine: Array<String>
    private var quantityOfLabel = 0
    private val dataMap = LinkedHashMap<Int, String>()
    private var startY = 0f
    private var positionOffset = 0
    private var viewWidth = 0
    private var maxPositionX = 0
    private lateinit var brokenLines: ArrayList<BrokenLine>

    override fun setData(data: LineChartData) {
        this.timeLine = data.timeLine.toStringDate()
        brokenLines = data.brokenLines
    }

    override fun draw(canvas: Canvas) {
        dataMap.forEach {
            val position = (it.key - positionOffset) * getStableX()
            val date = it.value
            canvas.drawText(date, position, startY, paintText)
        }
    }

    private var lastStepX = 0f
    private val minDiff = 0.08f

    private fun getStableX(): Float {
        val newStep = getStepX()
        if (lastStepX == 0f) lastStepX = newStep
        val difInPercent = Math.abs(lastStepX - newStep) / newStep
        if (difInPercent > minDiff) {
            lastStepX = newStep
        }
        return lastStepX
    }

    override fun setSize(viewWidth: Int, viewHeight: Int) {
        this.viewWidth = viewWidth
        maxPositionX = viewWidth - 75
        startY = viewHeight.toFloat() - 6
        quantityOfLabel = viewWidth / Constants.MIN_HORIZONTAL_SPACE - 1
    }

    override fun onFocusedRangeChanged(focusedRange: IntRange) {
        super.onFocusedRangeChanged(focusedRange)
        positionOffset = focusedRange.first
        dataMap.clear()
        defineDataToDraw(focusedRange)
        if (!isChartEnabled()) dataMap.clear()
    }

    private fun defineDataToDraw(range: IntRange) {
        var size = range.last - range.first
        if (size < quantityOfLabel) size = quantityOfLabel
        val stepToPick = size / quantityOfLabel

        for (index in 0 until timeLine.size step stepToPick) {
            dataMap[index] = timeLine[index]
        }
    }

    private fun isChartEnabled(): Boolean {
        return brokenLines.map { it.isEnabled }.any { it }
    }
}