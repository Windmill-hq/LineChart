package com.contest.chart.base

import android.graphics.Canvas
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants

abstract class AbstractChartController<LC : BaseLinePainter>(
        private val chartData: LineChartData,
        private val width: Int,
        private val height: Int,
        private val refresher: Refresher) : Focus {

    private val lineControllers = ArrayList<LC>()
    private var xScale = 0f
    private var yScale = 0f
    protected var focusRange = 0..100

    init {
        chartData.brokenLines.forEach {
            lineControllers.add(onCreateLinePainter(it, height))
        }
        calculateScale()
    }

    abstract fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): LC

    fun draw(canvas: Canvas) {
        lineControllers.forEach {
            it.draw(canvas, xScale, yScale)
        }
    }

    protected fun calculateScale() {
        val sizes = mutableListOf<Int>()
        chartData.brokenLines.forEach {
            if (it.isEnabled) sizes.add(getFocusedPoints(it).size)
        }

        if (sizes.isNotEmpty()) {
            xScale = (width - Constants.SPARE_SPACE_X) / sizes.max()!!.toFloat()
        }

        val maxValues = mutableListOf<Float>()
        chartData.brokenLines.forEach {
            if (it.isEnabled) maxValues.add(getFocusedPoints(it).max()!!)
        }

        if (maxValues.isNotEmpty()) {
            yScale = (height - Constants.SPARE_SPACE_Y) / maxValues.max()!!
        }
    }

    abstract fun getFocusedPoints(line: BrokenLine): FloatArray

    fun onFocusedRangeChanged(left: Int, right: Int) {
        lineControllers.get(0)?.let {
            val size = it.getPoints().size
            val focusLeft = size * left / 100
            val focusRight = size * right / 100
            focusRange = focusLeft..focusRight
        }
        notifyFocusRangeChanged()
        refresher.refresh()
    }

    abstract fun notifyFocusRangeChanged()

    fun onLineStateChanged(name: String, isShow: Boolean) {
        chartData.brokenLines.forEach { line ->
            if (line.name == name) line.isEnabled = isShow
        }
        calculateScale()
        refresher.refresh() // todo need hide/ show line with animation
    }

    override fun isFocused(pos: Int): Boolean {
        return pos in focusRange
    }

    fun getControllers(): List<LC> {
        return lineControllers
    }
}


