package com.contest.chart.base

import android.graphics.Canvas

interface BaseLinePainter {
    fun draw(canvas: Canvas, xScale: Float, yScale: Float)
    fun getPoints():FloatArray
}