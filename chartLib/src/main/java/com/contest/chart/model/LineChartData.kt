package com.contest.chart.model

import com.contest.chart.Constants

class LineChartData {
    var xScale = 0f
    var yScale = 0f

    fun setScale(width: Int, height: Int) {

        val sizes = mutableListOf<Int>()
        brokenLines.forEach {
            sizes.add(it.points.size)
        }
        val maxSize = sizes.max()!!.toFloat()

        xScale = (width - Constants.SPARE_SPACE_X) / maxSize

        val maxValues = mutableListOf<Float>()
        brokenLines.forEach {
            maxValues.add(it.points.max()!!)
        }

        yScale = (height - Constants.SPARE_SPACE_Y) / maxValues.max()!!
    }

    val brokenLines = ArrayList<BrokenLine>()
    lateinit var timeLine: LongArray
}