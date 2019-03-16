package com.contest.chart.refactor

import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.Focus
import com.contest.chart.base.Refresher
import com.contest.chart.base.LinePainter
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData

class BottomChartController(chartData: LineChartData, width: Int, height: Int, refresher: Refresher)
    : AbstractChartController<LinePainter>(chartData, width, height, refresher) {

    override fun onCreateLinePainter(line: BrokenLine, height: Int, focus: Focus): LinePainter {
        return LinePainter(line, height, focus)
    }
}