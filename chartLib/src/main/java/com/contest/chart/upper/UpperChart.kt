package com.contest.chart.upper

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
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

    override fun getStepMap(): Map<Int, Step> {
        val xStepMap = HashMap<Int, Step>()

        getControllers().forEach {
            val id = it.getId()
            xStepMap[id] = Step(it.xStep, it.yStep)
        }
        return xStepMap
    }

    override fun getPositionOffset(): Int {
        return getControllers()[0].getFocusedRange().first
    }

    override fun getTotalHeight(): Int {
        return viewHeight
    }
}
