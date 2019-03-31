package com.contest.chart.base

import android.graphics.Canvas
import android.graphics.Color
import com.contest.chart.model.BrokenLine
import com.contest.chart.utils.Constants
import com.contest.chart.utils.transparentOn

open class LinePrinter(
    line: BrokenLine,
    provider: DetalsProvider,
    thickness: Float
) : BaseLinePrinter(line, thickness, provider) {

    override fun draw(canvas: Canvas, xStep: Float, yStep: Float) {
        if (!line.isEnabled) return

        val size = line.points.size - 1
        for (positionX in 0 until size) {

            val x1 = positionX * xStep
            val x2 = (positionX + 1) * xStep

            val originY1 = line.points[positionX]
            val originY2 = line.points[positionX + 1]
            val y1 = getStartY() - originY1 * yStep
            val y2 = getStartY() - originY2 * yStep

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }
}