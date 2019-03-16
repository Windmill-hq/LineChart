package com.contest.chart.upper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import com.contest.chart.R
import com.contest.chart.base.MeasuredView

class DetailsView : MeasuredView {

    private val linePaint = Paint().apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val rectPaintStroke = Paint().apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val rectPaintFill = Paint().apply {
        color = resources.getColor(R.color.backGround)
        style = Paint.Style.FILL
    }

    private var rectTop = 5f
    private var rectWidth = 200f
    private var rectBottom = 155f
    private val radius = 10f

    private val detailsRect = RectF()

    private var positionX = 0f
    private var viewWidth = 0
    private var viewHeight = 0

    private var nightMode = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(positionX, 20f, positionX, viewHeight.toFloat(), linePaint)
        drawDetails(canvas)
    }

    private fun drawDetails(canvas: Canvas) {
        canvas.drawRoundRect(detailsRect, radius, radius, rectPaintFill)
        if (!nightMode) canvas.drawRoundRect(detailsRect, radius, radius, rectPaintStroke)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP -> {
                moveTo(event.x)
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun moveTo(x: Float) {
        positionX = x
        val left = positionX - 50
        detailsRect.set(left, rectTop, left + rectWidth, rectBottom)
        invalidate()
    }

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
    }

    override fun switchDayNightMode(nightMode: Boolean) {
        this.nightMode = nightMode
        val night = context.resources.getColor(R.color.detailsLineColorNight)
        val day = context.resources.getColor(R.color.detailsLineColor)
        linePaint.color = if (nightMode) night else day

        val nightDetail = context.resources.getColor(R.color.detailsBackgroundDark)
        val dayDetail = context.resources.getColor(R.color.backGround)
        rectPaintFill.color = if (nightMode) nightDetail else dayDetail
        invalidate()
    }
}
