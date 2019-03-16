package com.contest.chart.upper

import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.Refresher
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.getSampledPoints

class UpperChartController(
        chartData: LineChartData,
        width: Int,
        height: Int,
        refresher: Refresher)
    : AbstractChartController<UpperChatLinePainter>(chartData, width, height, refresher) {

    override fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): UpperChatLinePainter {
        return UpperChatLinePainter(line, conditionalY, this)
    }

    override fun getFocusedPoints(line: BrokenLine): FloatArray {
        return line.getSampledPoints(focusRange) // refactor and do not crete any new objects
    }

    override fun notifyFocusRangeChanged() {
        calculateScale()
        getControllers().forEach{
            it.offsetChanged(focusRange.first)
        }
    }
}