package com.telegram.chart

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent

class ActiveWindow {
    private val left = 5f
    private val top = 2f
    private val realWidth = 220f
    private val realHeight = 160f
    private val width = realWidth + left
    private val height = realHeight - top
    private var parentWidth = 0
    private var parentHeight = 0
    private val handleWidth = 15 + left
    private val scaleInc = 1.2f
    private val scaleDec = 0.8f
    private var partToDraw = Part.UNKNOWN

    private var mainRect = RectF(left, top, width, height)
    private var leftHandle = RectF(left, 0f, handleWidth, realHeight).apply {
        offsetTo(mainRect.left, mainRect.top - top)
    }
    private var rightHandle = RectF(leftHandle).apply {
        offsetTo(mainRect.right, mainRect.top - top)
    }

    private var paintMainRect: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    private var paintHandle: Paint = Paint().apply {
        color = Color.GRAY
    }

    private var listener: BottomControlView.Listener? = null

    fun draw(canvas: Canvas) {
        canvas.apply {
            drawRect(mainRect, paintMainRect)
            drawRect(leftHandle, paintHandle)
            drawRect(rightHandle, paintHandle)
        }
    }

    fun onMove(event: MotionEvent) {
        when (partToDraw) {
            Part.LEFT -> moveLeftHandle(event)
            Part.RIGHT -> moveRightHandle(event)
            Part.CENTER -> moveTogether(event)
        }
        if (partToDraw != Part.UNKNOWN) callback()
    }

    private fun moveRightHandle(event: MotionEvent) {
        if (checkMinWidth(event.x)) {
            val newLeft = event.x
            rightHandle.offsetTo(newLeft, rightHandle.top)
            mainRect.set(mainRect.left, mainRect.top, rightHandle.right, mainRect.bottom)
        }
    }

    private fun checkMinWidth(x: Float): Boolean {
        val minWidthForLeft = rightHandle.right - x
        val minWidthForRight = x - leftHandle.left

        return minWidthForLeft > realWidth || minWidthForRight > realWidth
    }

    private fun moveLeftHandle(event: MotionEvent) {
        if (checkMinWidth(event.x)) {
            val newLeft = event.x
            leftHandle.offsetTo(newLeft, leftHandle.top)
            mainRect.set(leftHandle.left, mainRect.top, mainRect.right, mainRect.bottom)
        }
    }

    private fun moveTogether(event: MotionEvent) {
        if (event.inView()) {
            val newLeft = event.x - totalWidth() / 2
            val newTop = mainRect.top
            mainRect.offsetTo(newLeft, newTop)
            leftHandle.offsetTo(mainRect.left, newTop - top)
            rightHandle.offsetTo(mainRect.right, newTop - top)
        }
    }

    fun onBeforeMove(event: MotionEvent) {
        partToDraw = when {
            isLeftHandle(event) -> Part.LEFT
            isRightHandle(event) -> Part.RIGHT
            isMainRect(event) -> Part.CENTER
            else -> Part.UNKNOWN
        }
    }

    fun onFinishMove() {
        partToDraw = Part.UNKNOWN
    }

    private fun totalWidth(): Float {
        return rightHandle.right - leftHandle.left
    }

    private fun isRightHandle(event: MotionEvent): Boolean {
        return rightHandle.scale().contains(event.x, event.y)
    }

    private fun isLeftHandle(event: MotionEvent): Boolean {
        return leftHandle.scale().contains(event.x, event.y)
    }

    private fun isMainRect(event: MotionEvent): Boolean {
        return mainRect.contains(event.x, event.y)
    }


    fun setParentSize(parentWidth: Int, parentHeight: Int) {
        this.parentWidth = parentWidth
        this.parentHeight = parentHeight
    }

    private fun callback() {
        listener?.onWindowSizeChanged(getLeftEdge(), getRightEdge())
    }

    private fun getRightEdge(): Float {
        return rightHandle.right
    }

    private fun getLeftEdge(): Float {
        return leftHandle.left
    }

    fun setListener(listener: BottomControlView.Listener) {
        this.listener = listener
    }

    private fun MotionEvent.inView(): Boolean {
        val maxX = x + totalWidth() / 2
        val minX = x
        return maxX < parentWidth && minX > totalWidth() / 2
    }

    private fun RectF.scale(): RectF {
        return RectF(this).apply {
            left *= scaleDec
            right *= scaleInc
        }
    }

    enum class Part {
        LEFT, RIGHT, CENTER, UNKNOWN
    }
}
