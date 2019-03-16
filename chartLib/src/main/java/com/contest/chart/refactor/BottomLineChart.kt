package com.contest.chart.refactor

import android.content.Context
import android.util.AttributeSet
import com.contest.chart.base.AbstractTimeBasedLineChart
import com.contest.chart.model.LineChartData

class BottomLineChart : AbstractTimeBasedLineChart<ChartController> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setData(dataList: List<LineChartData>) {
        dataList.forEach {
            chartControllers.add(ChartController(it, viewWidth, viewHeight, this))
        }
        isInited = true
    }
}