package com.contest.chart.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.model.BrokenLine
import com.contest.chart.utils.Constants
import com.contest.chart.utils.transparentOn

open class LinePainter(val line: BrokenLine,
                       private val conditionalY: Int,
                       private val focus: Focus) {

    private var unFocusedColor = line.color.transparentOn(Constants.TRANSPARENCY)
    private val focusedColor = Color.parseColor(line.color)
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    fun draw(canvas: Canvas, xScale: Float, yScale: Float) {
        if (!line.isEnabled) return

        val size = line.points.size - 1
        for (positionX in 0 until size) {

            paint.color = detectColor(positionX)

            val x1 = positionX * xScale
            val x2 = (positionX + 1) * xScale

            val originY1 = line.points[positionX]
            val originY2 = line.points[positionX + 1]
            val y1 = conditionalY - originY1 * yScale
            val y2 = conditionalY - originY2 * yScale

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }

    private fun detectColor(index: Int): Int {
        return if (focus.isFocused(index)) focusedColor else unFocusedColor
    }
}