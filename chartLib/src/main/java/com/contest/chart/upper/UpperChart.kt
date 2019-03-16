package com.contest.chart.upper

import android.content.Context
import android.util.AttributeSet
import com.contest.chart.base.AbstractTimeBasedLineChart
import com.contest.chart.model.LineChartData

class UpperChart : AbstractTimeBasedLineChart<UpperChartController> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onCreateController(data: LineChartData): UpperChartController {
        return UpperChartController(data, viewWidth, viewHeight, this)
    }

}