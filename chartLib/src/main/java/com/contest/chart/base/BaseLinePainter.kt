package com.contest.chart.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.model.BrokenLine

abstract class BaseLinePainter(
    val line: BrokenLine,
    private val thickness: Float,
    val provider: DetalsProvider
) : AbstractLinePainter {

    protected val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        style = Paint.Style.STROKE
        strokeWidth = thickness
        color = Color.parseColor(line.color)
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor(line.color)
    }

    override fun getPoints(): FloatArray {
        return line.points
    }

    override fun getStartY(): Int {
        return provider.getStartY()
    }

    protected fun makeSmooth(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, thickness / 2, circlePaint)
    }
}