package com.contest.chart.base

import android.graphics.Canvas

interface AbstractLinePainter {
    fun draw(canvas: Canvas, xScale: Float, yScale: Float)
    fun getPoints():FloatArray
}