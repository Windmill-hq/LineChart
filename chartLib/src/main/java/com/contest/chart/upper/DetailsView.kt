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

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    private val green = resources.getColor(R.color.green)
    private val red = resources.getColor(R.color.red)
    private val black = resources.getColor(R.color.black)

    private val detailsRect = RectF()

    private var rectTop = 5f
    private var rectWidth = 200f
    private var rectBottom = 155f
    private val radius = 10f
    private var positionX = 0f
    private var viewWidth = 0
    private var viewHeight = 0
    private val xOffset = 50
    private var nightMode = false

    private var dateLabel = "Sat, May 4"
    private var firstValue = 127
    private var firstLabel = "Joined"
    private var secondValue = 69
    private var secondLabel = "Left"
    private var inited = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(positionX, 20f, positionX, viewHeight.toFloat(), linePaint)
        drawDetails(canvas)
    }

    private fun drawDetails(canvas: Canvas) {
        if (!inited) return
        canvas.drawRoundRect(detailsRect, radius, radius, rectPaintFill)
        if (!nightMode) canvas.drawRoundRect(detailsRect, radius, radius, rectPaintStroke)
        drawData(canvas)
    }

    private fun drawData(canvas: Canvas) {
        var x = detailsRect.left + 35
        var y = detailsRect.top + 35
        canvas.drawText(dateLabel, x, y, blackPaint())

        y += 50
        canvas.drawText(firstValue.toString(), x, y, greenPaint())
        y += 20
        canvas.drawText(firstLabel, x, y, greenPaint())

        x = detailsRect.left + 100
        y = detailsRect.top + 85
        canvas.drawText(secondValue.toString(), x, y, redPaint())

        y += 20
        canvas.drawText(secondLabel, x, y, redPaint())

    }

    private fun redPaint(): Paint {
        return textPaint.apply { color = red }
    }

    private fun greenPaint(): Paint {
        return textPaint.apply { color = green }
    }

    private fun blackPaint(): Paint {
        return textPaint.apply { color = black }
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
        inited = true
        positionX = x
        if (isBlockInView(x)) {
            val left = positionX - xOffset
            detailsRect.set(left, rectTop, left + rectWidth, rectBottom)
        }
        invalidate()
    }

    private fun isBlockInView(x: Float): Boolean {
        return x > xOffset && x + rectWidth - xOffset < viewWidth
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
