package com.contest.chart

interface ChartDetailsProvider {
    fun getChartStep(): Step
    fun getTotalHeight(): Int
    fun getPositionOffset(): Int
}

class Step(var xStep: Float, var yStep: Float)