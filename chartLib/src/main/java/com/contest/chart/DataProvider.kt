package com.contest.chart

import com.contest.chart.model.ChartDetail

interface DataProvider {
    fun requestData(x: Float, y: Float): List<ChartDetail>
    fun setStepProvider(stepProvider: StepProvider)
}