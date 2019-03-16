package com.contest.chart.bottom

import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.LinePainter
import com.contest.chart.base.Refresher
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData

class BottomChartController(chartData: LineChartData, width: Int, height: Int, refresher: Refresher)
    : AbstractChartController<LinePainter>(chartData, width, height, refresher) {

    override fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): LinePainter {
        return LinePainter(line, conditionalY, this)
    }

    override fun getFocusedPoints(line: BrokenLine): FloatArray {
        return line.points
    }

    override fun notifyFocusRangeChanged() {
    }
}