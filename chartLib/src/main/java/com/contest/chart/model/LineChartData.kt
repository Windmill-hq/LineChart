package com.contest.chart.model

class LineChartData {
    var horizontalStep = 0f

    fun setStep(parentWidth: Int) {

        val sizes = mutableListOf<Int>()
        brokenLines.forEach {
            sizes.add(it.points.size)
        }
        val maxSize = sizes.max()!!.toFloat()

        horizontalStep = parentWidth / maxSize
    }

    val brokenLines = ArrayList<BrokenLine>()
    lateinit var timeLine: LongArray
}