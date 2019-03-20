package com.contest.chart.base

import android.graphics.Canvas

interface AbstractLinePainter {
    fun draw(canvas: Canvas, xStep: Float, yStep: Float)
    fun getPoints(): FloatArray
    fun getStartY(): Int
}