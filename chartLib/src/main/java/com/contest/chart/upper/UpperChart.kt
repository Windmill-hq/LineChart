package com.contest.chart.upper

import android.content.Context
import android.util.AttributeSet
import com.contest.chart.Step
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.base.AbstractLineChart
import com.contest.chart.model.LineChartData

class UpperChart : AbstractLineChart<UpperChartController>, ChartDetailsProvider {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onCreateController(data: LineChartData): UpperChartController {
        return UpperChartController(data, viewWidth, viewHeight, this)
    }

    override fun getChartStep(): Step {
        return Step(getController().xStep, getController().yStep)
    }

    override fun getPositionOffset(): Int {
        return getController().getFocusedRange().first
    }

    override fun getTotalHeight(): Int {
        return viewHeight
    }
}
