package com.contest.chart.details

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.contest.chart.R

class DetailsWindow(resources: Resources) {
    private var viewWidth = 0
    private var viewHeight = 0

    private var positionX = 0f
    private var rectTop = 5f
    private var rectWidth = 200f
    private var rectBottom = 155f
    private val radius = 10f
    private val xOffset = 50
    private var nightMode = false

    private val rectangle = RectF()

    val left: Float
        get() {
            return rectangle.left
        }

    val top: Float
        get() {
            return rectangle.top
        }

    private val rectPaintStroke = Paint().apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val linePaint = Paint().apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val rectPaintFill = Paint().apply {
        color = resources.getColor(R.color.backGround)
        style = Paint.Style.FILL
    }

    fun draw(canvas: Canvas) {
        canvas.drawLine(positionX, rectBottom, positionX, viewHeight.toFloat(), linePaint)
        canvas.drawRoundRect(rectangle, radius, radius, rectPaintFill)
        if (!nightMode) canvas.drawRoundRect(rectangle, radius, radius, rectPaintStroke)
    }

    fun setSize(width: Int, height: Int) {
        viewWidth = width
        viewHeight = height
    }

    fun moveTo(x: Float) {
        positionX = x
        if (isBlockInView(x)) {
            val left = positionX - xOffset
            val right = left + rectWidth
            rectangle.set(left, rectTop, right, rectBottom)
        }
    }

    private fun isBlockInView(x: Float): Boolean {
        return x > xOffset && x + rectWidth - xOffset < viewWidth
    }

    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        this.nightMode = nightMode
        val night = resources.getColor(R.color.detailsLineColorNight)
        val day = resources.getColor(R.color.detailsLineColor)
        linePaint.color = if (nightMode) night else day

        val nightDetail = resources.getColor(R.color.detailsBackgroundDark)
        val dayDetail = resources.getColor(R.color.backGround)
        rectPaintFill.color = if (nightMode) nightDetail else dayDetail
    }
}