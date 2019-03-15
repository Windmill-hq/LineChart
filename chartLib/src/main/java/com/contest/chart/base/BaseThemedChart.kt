package com.contest.chart.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.contest.chart.R

abstract class BaseThemedChart : MeasuredView {
    private val paintBackGround = Paint().apply { color = Color.WHITE }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(paintBackGround)
    }

    fun switchDayNightMode(nightMode: Boolean) {
        val night = context.resources.getColor(R.color.backGroundDark)
        val day = context.resources.getColor(R.color.backGround)
        paintBackGround.color = if (nightMode) night else day
        invalidate()
    }
}