package com.contest.chart.model

class LineChartData(val id: Int) {
    val brokenLines = ArrayList<BrokenLine>()
    lateinit var timeLine: LongArray
}