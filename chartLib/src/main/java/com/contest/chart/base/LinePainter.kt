package com.contest.chart.base

import android.graphics.Canvas
import android.graphics.Color
import com.contest.chart.model.BrokenLine
import com.contest.chart.utils.Constants
import com.contest.chart.utils.transparentOn

open class LinePainter(
        line: BrokenLine,
        private val conditionalY: Int,
        private val focus: Focus,
        thickness: Float) : BaseLinePainter(line, thickness) {

    private var unFocusedColor = line.color.transparentOn(Constants.TRANSPARENCY)
    private val focusedColor = Color.parseColor(line.color)

    override fun draw(canvas: Canvas, xStep: Float, yStep: Float) {
        if (!line.isEnabled) return

        val size = line.points.size - 1
        for (positionX in 0 until size) {

            paint.color = detectColor(positionX)

            val x1 = positionX * xStep
            val x2 = (positionX + 1) * xStep

            val originY1 = line.points[positionX]
            val originY2 = line.points[positionX + 1]
            val y1 = conditionalY - originY1 * yStep
            val y2 = conditionalY - originY2 * yStep

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }

    private fun detectColor(index: Int): Int {
        return if (focus.isFocused(index)) focusedColor else unFocusedColor
    }
}