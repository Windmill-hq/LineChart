package com.contest.chart

interface ChartDetailsProvider {
    fun getChartStep(): Step
    fun getTotalHeight(): Int
    fun getPositionOffset(): Int
}

class Step(val xStep: Float, val yStep: Float)