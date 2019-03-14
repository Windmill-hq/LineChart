package com.contest.chart.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent

class FocusWindow(private val frameColor: Int) {
    private val left = 25f
    private val top = 2f
    private val realRectWidth = 220f
    private val realHeight = 160f
    private var parentWidth = 0
    private var parentHeight = 0
    private val handleWidth = 15
    private var partToDraw = Part.UNKNOWN
    private val rectStrokeWidth = 6f
    private val halfStroke = rectStrokeWidth / 2

    private val mainLeft = left + handleWidth + halfStroke
    private val mainRight = mainLeft + realRectWidth
    private val leftHandleRightPos = handleWidth + left
    private val rectHeight = realHeight - top
    private var mainRect = RectF(mainLeft, top, mainRight, rectHeight)
    private var leftHandle = RectF(left, 0f, leftHandleRightPos, realHeight)

    private var rightHandle = RectF(leftHandle).apply {
        val rightHandleLeft = mainRect.right + halfStroke
        offsetTo(rightHandleLeft, 0f)
    }

    private var paintMainRect: Paint = Paint().apply {
        color = frameColor
        style = Paint.Style.STROKE
        strokeWidth = rectStrokeWidth
    }

    private var paintHandle: Paint = Paint().apply {
        color = frameColor
    }

    private var listeners = ArrayList<Listener>()

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
        if (checkMinWidthRight(event.x)) {
            val newLeft = event.x
            rightHandle.offsetTo(newLeft, rightHandle.top)
            mainRect.set(mainRect.left, mainRect.top, rightHandle.left - halfStroke, mainRect.bottom)
        }
    }

    private fun checkMinWidthRight(x: Float): Boolean {
        val distanceToLeftEdge = x - leftHandle.left
        return distanceToLeftEdge > realRectWidth
    }

    private fun checkMinWidthLeft(x: Float): Boolean {
        val distanceToRightEdge = rightHandle.right - x
        return distanceToRightEdge > realRectWidth
    }

    private fun moveLeftHandle(event: MotionEvent) {
        if (checkMinWidthLeft(event.x)) {
            val newLeft = event.x
            leftHandle.offsetTo(newLeft, leftHandle.top)
            mainRect.set(leftHandle.right + halfStroke, mainRect.top, mainRect.right, mainRect.bottom)
        }
    }

    private fun moveTogether(event: MotionEvent) {
        if (event.inView()) {
            val newLeft = event.x - totalWidth() / 2
            val newTop = mainRect.top
            mainRect.offsetTo(newLeft, newTop)
            leftHandle.offsetTo(mainRect.left - halfStroke - handleWidth, newTop - top)
            rightHandle.offsetTo(mainRect.right + halfStroke, newTop - top)
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
        return mainRect.right - mainRect.left
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

    public fun callback() {
        listeners.forEach {
            it.onFocusWindowSizeChanged(
                ((mainRect.left / parentWidth) * 100).toInt(),
                ((mainRect.right / parentWidth) * 100).toInt()
            )
        }
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    private fun MotionEvent.inView(): Boolean {
        val maxX = x + totalWidth() / 2
        val minX = x
        return maxX < parentWidth && minX > totalWidth() / 2
    }

    private fun RectF.scale(): RectF {
        return RectF(this).apply {
            left -= 30
            right += 30
        }
    }

    enum class Part {
        LEFT, RIGHT, CENTER, UNKNOWN
    }

    interface Listener {
        fun onFocusWindowSizeChanged(left: Int, right: Int)
    }
}
