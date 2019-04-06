package com.contest.chart.bottom

import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.ChartView
import com.contest.chart.base.LinePrinter
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getChartMaxSize
import com.contest.chart.utils.getChartMaxValue

class BottomChartController(chartData: LineChartData, chartView: ChartView) :
    AbstractChartController<LinePrinter>(chartData, chartView) {

    override fun onCreateLinePainter(line: BrokenLine): LinePrinter {
        return LinePrinter(line, this, Constants.BOTTOM_CHART_LINE_THICKNESS)
    }

    override fun getMaxValue(): Float {
        return chartData.getChartMaxValue(yStepStore)
    }

    override fun getMaxSize(): Int {
        return chartData.getChartMaxSize(xStepStore)
    }

    init {
        calculateSteps()
    }

    override fun calculateSteps() {
        calculateVerticalStep()
        calculateHorizontalStepNoAnim()
    }

    override fun getFocusedRange(): IntRange {
        return IntRange.EMPTY
    }
}