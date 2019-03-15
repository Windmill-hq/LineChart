package com.contest.chart

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.transparentOn

class MultiLineChartControllerUpper(
        private val chartData: LineChartData,
        private val width: Int,
        private val height: Int,
        private val refresher: Refresher
) : Focus {

    private val lineControllers = ArrayList<LinePainterUpper>()
    private var xScale = 0f
    private var yScale = 0f
    private var focusRange = 0..100

    init {
        chartData.brokenLines.forEach {
            lineControllers.add(LinePainterUpper(it, height, this))
        }
        calculateScale()
    }

    fun draw(canvas: Canvas) {
        lineControllers.forEach {
            it.draw(canvas, xScale, yScale, focusRange.first)
        }
    }

    private fun calculateScale() {
        val sizes = mutableListOf<Int>()
        chartData.brokenLines.forEach {
            if (it.isEnabled) {
                sizes.add(it.getSampledPoints().size)
            }
        }

        if (sizes.isNotEmpty()) {
            xScale = (width - Constants.SPARE_SPACE_X) / sizes.max()!!.toFloat()
        }

        val maxValues = mutableListOf<Float>()
        chartData.brokenLines.forEach {
            if (it.isEnabled) {
                maxValues.add(it.getSampledPoints().max()!!)
            }
        }

        if (maxValues.isNotEmpty()) {
            yScale = (height - Constants.SPARE_SPACE_Y) / maxValues.max()!!
        }
    }

    fun onFocusedRangeChanged(left: Int, right: Int) {
        lineControllers.get(0)?.let {
            val size = it.line.points.size
            val focusLeft = size * left / 100
            val focusRight = size * right / 100
            focusRange = focusLeft..focusRight
        }

        calculateScale()
        refresher.refresh()
    }

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


    private fun BrokenLine.getSampledPoints(): FloatArray {
        return this.points.copyOfRange(focusRange.first, focusRange.last)
    }
}

open class LinePainterUpper(
        val line: BrokenLine,
        private val conditionalY: Int,
        private val focus: Focus
) {

    private var unFocusedColor = line.color.transparentOn(Constants.TRANSPARENCY)
    private val focusedColor = Color.parseColor(line.color)
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = focusedColor
    }

    fun draw(canvas: Canvas, xScale: Float, yScale: Float, offset: Int) {
        if (!line.isEnabled) return

        val size = line.points.size - 1
        for (positionX in 0 until size) {
            if (focus.isFocused(positionX)) {
                val x1 = (positionX - offset) * xScale
                val x2 = (positionX + 1 - offset) * xScale

                val originY1 = line.points[positionX]
                val originY2 = line.points[positionX + 1]
                val y1 = conditionalY - originY1 * yScale
                val y2 = conditionalY - originY2 * yScale

                canvas.drawLine(x1, y1, x2, y2, paint)
            }
        }
    }
}
