package com.contest.chart.details

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.contest.chart.R
import com.contest.chart.utils.getColor

class DetailsWindow(resources: Resources) {
    private var viewWidth = 0
    private var viewHeight = 0

    private var positionX = 0f
    private var rectTop = 5f
    private var rectWidth = 330f
    private var rectBottom = 50f
    private val radius = 10f
    private val xOffset = 30
    private var nightMode = false

    private val statisticWidth = 40f
    val rectangle = RectF()

    val left: Float
        get() {
            return rectangle.left
        }

    val top: Float
        get() {
            return rectangle.top
        }

    private val rectPaintStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val rectPaintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.detailsBackground)
        style = Paint.Style.FILL
    }

    fun draw(canvas: Canvas) {
        canvas.drawLine(positionX, 0f, positionX, viewHeight.toFloat(), linePaint)
        canvas.drawRoundRect(rectangle, radius, radius, rectPaintFill)
        if (!nightMode) canvas.drawRoundRect(rectangle, radius, radius, rectPaintStroke)
    }

    fun setSize(width: Int, height: Int) {
        viewWidth = width
        viewHeight = height
    }

    fun moveTo(x: Float, size: Int) {
        positionX = x
        if (isBlockInView(x)) {
            val left = positionX + xOffset
            val right = left + rectWidth
            rectangle.set(left, rectTop, right, rectBottom + (statisticWidth * size))
        }
    }

    private fun isBlockInView(x: Float): Boolean {
        val width = getRectWidth()
        return x > 0 && x + width + xOffset < viewWidth
    }

    private fun getRectWidth(): Float {
        return rectangle.right - rectangle.left
    }

    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        this.nightMode = nightMode
        linePaint.color = resources.getColor(R.color.detailsLineColor, R.color.detailsLineColorNight, nightMode)
        rectPaintFill.color = resources.getColor(R.color.detailsBackground, R.color.detailsBackgroundDark, nightMode)
    }
}