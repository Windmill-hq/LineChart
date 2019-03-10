package com.telegram.chart

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent

class ActiveWindow {
    private val left = 5f
    private val top = 2f
    private val realWidth = 160f
    private val realHeight = 160f
    private val width = realWidth + left
    private val height = realHeight - top
    private var parentWidth = 0
    private var parentHeight = 0
    private val handleWidth = 15 + left

    private var mainRect = RectF(left, top, width, height)
    private var leftHandle = RectF(left, 0f, handleWidth, realHeight).apply {
        offsetTo(mainRect.left, mainRect.top - top)
    }
    private var rightHandle = RectF(leftHandle).apply {
        offsetTo(mainRect.right, mainRect.top - top)
    }

    private var paintStroke: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    private var paintHandle: Paint = Paint().apply {
        color = Color.GRAY
    }

    fun draw(canvas: Canvas) {
        canvas.apply {
            drawRect(mainRect, paintStroke)
            drawRect(leftHandle, paintHandle)
            drawRect(rightHandle, paintHandle)
        }
    }

    fun onMove(event: MotionEvent) {
        if (isInside(event)) {
            moveTogether(event)
        }
    }

    private fun isInside(event: MotionEvent): Boolean {
        return mainRect.contains(event.x, event.y)
    }

    private fun moveTogether(event: MotionEvent) {
        if (inView(event)) {
            val newLeft = event.x - width / 2
            val newTop = mainRect.top
            mainRect.offsetTo(newLeft, newTop)
            leftHandle.offsetTo(mainRect.left, newTop - top)
            rightHandle.offsetTo(mainRect.right, newTop - top)
        }
    }

    private fun inView(event: MotionEvent): Boolean {
        val maxX = event.x + width / 2
        val minX = event.x
        return maxX < parentWidth && minX > width / 2
    }

    fun setParentSize(parentWidth: Int, parentHeight: Int) {
        this.parentWidth = parentWidth
        this.parentHeight = parentHeight
    }
}