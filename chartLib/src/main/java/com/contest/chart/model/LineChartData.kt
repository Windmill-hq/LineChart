package com.contest.chart.model

import com.contest.chart.Constants

class LineChartData {
    var xScale = 0f
    var yScale = 0f

    private var width = 0
    private var height = 0

    fun setScale() {

        val sizes = mutableListOf<Int>()
        brokenLines.forEach {
            if (it.isEnabled) sizes.add(it.points.size)
        }
        val maxSize = sizes.max()
        maxSize?.run {
            xScale = (width - Constants.SPARE_SPACE_X) / this.toFloat()

            val maxValues = mutableListOf<Float>()
            brokenLines.forEach {
                if (it.isEnabled) maxValues.add(it.points.max()!!)
            }

            yScale = (height - Constants.SPARE_SPACE_Y) / maxValues.max()!!
        }
    }

    fun setSize(viewWidth: Int, viewHeight: Int) {
        this.width = viewWidth
        this.height = viewHeight
        setScale()
    }

    val brokenLines = ArrayList<BrokenLine>()
    lateinit var timeLine: LongArray
}