package com.contest.chart.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.contest.chart.model.BrokenLine

abstract class BaseLinePrinter(
    val line: BrokenLine,
    private val thickness: Float,
    val provider: DetalsProvider
) : AbstractLinePainter {
    protected val path = Path()

    protected val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = thickness
        color = Color.parseColor(line.color)
    }

    override fun getPoints(): FloatArray {
        return line.points
    }

    override fun getStartY(): Int {
        return provider.getStartY()
    }

    fun drawSimpleBottomLine(canvas: Canvas, xStep: Float, yStep: Float) {

        path.reset()
        val size = line.points.size - 1

        for (positionX in 0 until size) {

            val x1 = positionX * xStep
            val x2 = (positionX + 1) * xStep

            val originY1 = line.points[positionX]
            val originY2 = line.points[positionX + 1]
            val y1 = getStartY() - originY1 * yStep
            val y2 = getStartY() - originY2 * yStep

            path.moveTo(x1, y1)
            path.lineTo(x2, y2)
        }

        canvas.drawPath(path, paint)
    }
}