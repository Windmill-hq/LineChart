package com.contest.chart.upper

import android.graphics.Canvas
import com.contest.chart.base.BaseLinePainter
import com.contest.chart.base.Focus
import com.contest.chart.model.BrokenLine

class UpperChatLinePainter(
        line: BrokenLine,
        private val conditionalY: Int,
        private val focus: Focus,
        thickness: Float)
    : BaseLinePainter(line, thickness) {

    private var positionOffset = 0

    override fun draw(canvas: Canvas, xStep: Float, yStep: Float) {
        if (!line.isEnabled) return

        val range = focus.getFocusedRange()

        for (positionX in range) {
            val x1 = (positionX - positionOffset) * xStep
            val x2 = (positionX + 1 - positionOffset) * xStep

            val originY1 = line.points[positionX]
            val originY2 = line.points[positionX + 1]
            val y1 = conditionalY - originY1 * yStep
            val y2 = conditionalY - originY2 * yStep

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }

    fun offsetChanged(newOffset: Int) {
        positionOffset = newOffset
    }
}