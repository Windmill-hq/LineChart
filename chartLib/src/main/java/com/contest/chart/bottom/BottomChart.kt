package com.contest.chart.bottom

import android.content.Context
import android.util.AttributeSet
import com.contest.chart.base.AbstractTimeBasedLineChart
import com.contest.chart.base.Refresher
import com.contest.chart.model.LineChartData

class BottomChart : AbstractTimeBasedLineChart<BottomChartController> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onCreateController(data: LineChartData, viewWidth: Int, viewHeight: Int, refresher: Refresher): BottomChartController {
        return BottomChartController(data, viewWidth, viewHeight, refresher)
    }
}