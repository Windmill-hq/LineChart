package com.contest.chart.upper

import android.graphics.Canvas
import com.contest.chart.base.BaseLinePainter
import com.contest.chart.base.Focus
import com.contest.chart.model.BrokenLine

class UpperChatLinePainter(
        line: BrokenLine,
        private val conditionalY: Int,
        private val focus: Focus)
    : BaseLinePainter(line) {

    private var positionOffset = 0

    override fun draw(canvas: Canvas, xScale: Float, yScale: Float) {
        if (!line.isEnabled) return

        val size = line.points.size - 1
        for (positionX in 0 until size) {
            if (focus.isFocused(positionX)) {
                val x1 = (positionX - positionOffset) * xScale
                val x2 = (positionX + 1 - positionOffset) * xScale

                val originY1 = line.points[positionX]
                val originY2 = line.points[positionX + 1]
                val y1 = conditionalY - originY1 * yScale
                val y2 = conditionalY - originY2 * yScale

                canvas.drawLine(x1, y1, x2, y2, paint)
            }
        }
    }

    fun offsetChanged(newOfset: Int) {
        positionOffset = newOfset
    }
}