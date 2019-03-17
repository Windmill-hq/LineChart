package com.contest.chart

import com.contest.chart.model.InterceptionInfo

interface DataProvider {
    fun getInterceptions(x: Float, offset: Int): List<InterceptionInfo>
    fun setChartDetailsProvider(chartDetailsProvider: ChartDetailsProvider)
}