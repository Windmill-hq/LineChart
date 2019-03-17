package com.contest.chart.base

import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.model.BrokenLine

abstract class BaseLinePainter(val line: BrokenLine, thickness: Float) : AbstractLinePainter {

    protected val paint = Paint().apply {
        style = Paint.Style.FILL
        style = Paint.Style.STROKE
        strokeWidth = thickness
        color = Color.parseColor(line.color)
    }

    override fun getPoints(): FloatArray {
        return line.points
    }
}