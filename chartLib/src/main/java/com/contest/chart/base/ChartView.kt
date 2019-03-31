package com.contest.chart.base

interface ChartView {
    fun update()
    fun getChartWidth(): Int
    fun getChartHeight(): Int
}