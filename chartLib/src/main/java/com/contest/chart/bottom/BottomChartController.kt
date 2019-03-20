package com.contest.chart.bottom

import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.LinePainter
import com.contest.chart.base.Refresher
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getChartMaxSize
import com.contest.chart.utils.getChartMaxValue

class BottomChartController(chartData: LineChartData, width: Int, height: Int, refresher: Refresher)
    : AbstractChartController<LinePainter>(chartData, width, height, refresher) {

    override fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): LinePainter {
        return LinePainter(line, conditionalY, this, Constants.BOTTOM_CHART_LINE_THICKNESS)
    }

    override fun getMaxValue(): Float {
        return chartData.getChartMaxValue(yStepStore)
    }

    override fun getMaxSize(): Int {
        return chartData.getChartMaxSize(xStepStore)
    }

    override fun notifyFocusRangeChanged() {
    }

    override fun calculateSteps() {
        calculateHorizontalStepNoAnim()
        calculateVerticalStepNoAnim()
    }

    private fun calculateHorizontalStepNoAnim() {
        val maxSize = getMaxSize()
        if (maxSize == 0) return
        horizontalStep = width / maxSize.toFloat()
    }

    private fun calculateVerticalStepNoAnim() {
        val maxVal = getMaxValue()
        verticalStep = (height - Constants.SPARE_VERTICAL_SPACE) / maxVal
    }
}