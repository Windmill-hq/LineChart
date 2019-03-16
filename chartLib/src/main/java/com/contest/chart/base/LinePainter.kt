package com.contest.chart.base

import android.graphics.Canvas
import com.contest.chart.model.BrokenLine

open class LinePainter(
        line: BrokenLine,
        private val conditionalY: Int) : BaseLinePainter(line) {

    override fun draw(canvas: Canvas, xScale: Float, yScale: Float) {
        if (!line.isEnabled) return

        val size = line.points.size - 1
        for (positionX in 0 until size) {

            val x1 = positionX * xScale
            val x2 = (positionX + 1) * xScale

            val originY1 = line.points[positionX]
            val originY2 = line.points[positionX + 1]
            val y1 = conditionalY - originY1 * yScale
            val y2 = conditionalY - originY2 * yScale

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }
}