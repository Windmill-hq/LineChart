package com.contest.chart.upper

import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.Refresher
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getChartMaxSize
import com.contest.chart.utils.getChartMaxValueInRange

class UpperChartController(
        chartData: LineChartData,
        width: Int,
        height: Int,
        refresher: Refresher
) : AbstractChartController<UpperChatLinePainter>(chartData, width, height, refresher) {

    override fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): UpperChatLinePainter {
        return UpperChatLinePainter(line, conditionalY, this, Constants.UPPER_CHART_LINE_THICKNESS)
    }

    override fun notifyFocusRangeChanged() {
        getControllers().forEach {
            it.offsetChanged(focusRange.first)
        }
    }

    override fun getMaxValue(): Float {
        return chartData.getChartMaxValueInRange(focusRange, yStepStore)
    }

    override fun getMaxSize(): Int {
        return chartData.getChartMaxSize(xStepStore, focusRange)
    }
}

