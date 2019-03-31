package com.contest.chart.bottom

import android.content.Context
import android.util.AttributeSet
import com.contest.chart.base.AbstractLineChart
import com.contest.chart.model.LineChartData

class BottomChart : AbstractLineChart<BottomChartController> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onCreateController(data: LineChartData): BottomChartController {
        return BottomChartController(data, this)
    }
}