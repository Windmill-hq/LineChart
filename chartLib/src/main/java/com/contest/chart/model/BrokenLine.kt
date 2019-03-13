package com.contest.chart.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class BrokenLine(val points: FloatArray, val name: String, val color: String?) {
    private var yOffset: Int = 0
    private val unFocusedColor = Color.parseColor("#a8a8a8")
    private val focusedColor = Color.parseColor(color)
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private var focusLeft = 0
    private var focusRight = 0

    fun draw(canvas: Canvas, horizontalStep: Float) {

        val size = points.size

        for (index in 0 until size - 1) {

            paint.color = getColor(index)

            val x1 = index * horizontalStep
            val x2 = (index + 1) * horizontalStep

            val y1 = points[index]
            val y2 = points[index + 1]

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }

    private fun getColor(index: Int): Int {
        return if (index in focusLeft..focusRight) focusedColor else unFocusedColor
    }


    fun setYTo(parentHeight: Int) {
        yOffset = parentHeight
    }

    fun onFocusedWindowSizeChanged(left: Int, right: Int) {
        focusLeft = points.size * left / 100
        focusRight = points.size * right / 100
    }

}
