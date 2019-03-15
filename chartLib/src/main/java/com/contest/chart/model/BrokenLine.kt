package com.contest.chart.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.Constants
import java.lang.StringBuilder

class BrokenLine(val points: FloatArray, val name: String, private val color: String) {
    var isEnabled = true
    private var conditionalY: Int = 0
    private var unFocusedColor = color.transparentOn(Constants.TRANSPARENCY)
    private val focusedColor = Color.parseColor(color)
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private var focusRange = 0..100

    fun draw(canvas: Canvas, xScale: Float, yScale: Float) {
        if (!isEnabled) return

        val size = points.size

        for (index in 0 until size - 1) {

            paint.color = getColor(index)

            val x1 = index * xScale
            val x2 = (index + 1) * xScale

            val y1 = conditionalY - (points[index] * yScale)
            val y2 = conditionalY - (points[index + 1] * yScale)

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }

    private fun getColor(index: Int): Int {
        return if (index in focusRange) focusedColor else unFocusedColor
    }


    fun setConditionalY(conditionalY: Int) {
        this.conditionalY = conditionalY
    }

    fun onFocusedRangeChanged(left: Int, right: Int) {
        val focusLeft = points.size * left / 100
        val focusRight = points.size * right / 100
        focusRange = focusLeft..focusRight
    }

    fun show(show: Boolean) {
        isEnabled = show
        if (!isEnabled) {
            hideAnimated()
        }
    }

    private fun hideAnimated() {
        //todo   need remove line with animation and call invalidate from here
    }

}

private fun String.transparentOn(transparency: String): Int {
    val pale = StringBuilder(this).insert(1, transparency).toString()
    return Color.parseColor(pale)
}
