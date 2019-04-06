package com.contest.chart.base

import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.model.BrokenLine

abstract class BaseLinePrinter(
    val line: BrokenLine,
    private val thickness: Float,
    val provider: DetalsProvider
) : AbstractLinePainter {

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
}