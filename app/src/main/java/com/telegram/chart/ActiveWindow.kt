package com.telegram.chart

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent

class ActiveWindow {
    private var rect = RectF(20f, 20f, 120f, 120f)
    private val DEBUG_TAG = "Chart gesture"

    private var paintRect: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    fun isInside(event: MotionEvent): Boolean {
        return rect.contains(event.x, event.y)
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paintRect)
    }

    fun move(event: MotionEvent) {
        rect.offsetTo(event.x, rect.top)
    }
}