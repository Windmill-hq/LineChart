package com.contest.chart.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import com.contest.chart.LockableScrollView
import com.contest.chart.R
import com.contest.chart.utils.getColor

class FocusedRangeFrame : MeasuredView {

    private val left = 25f
    private val top = 2f
    private val realFrameWidth = 200f
    private val minFrameWidth = 100f
    private var viewWidth = 0
    private var viewHeight = 0
    private val handleWidth = 15
    private val rectStrokeWidth = 6f
    private val halfStroke = rectStrokeWidth / 2

    private val mainFrameLeft = left + handleWidth + halfStroke
    private val mainFrameRight = mainFrameLeft + realFrameWidth
    private val leftHandleRight = handleWidth + left

    private var mainFrame = RectF()
    private var leftHandle = RectF()
    private var rightHandle = RectF()
    private var rightShadow = RectF()
    private var leftShadow = RectF()

    private var paintHandle: Paint = Paint()
    private var paintMainRect: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = rectStrokeWidth
    }
    private val shadowPaint = Paint()
    private var listeners = ArrayList<Listener>()
    private var partToDraw = Part.UNKNOWN

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val frameColor = context.resources.getColor(R.color.frameColor)
        paintMainRect.color = frameColor
        paintHandle.color = frameColor
        shadowPaint.color = context.resources.getColor(R.color.shadow)
    }

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
        reCalculate()
        getFocusedRange()
    }

    private fun reCalculate() {
        mainFrame.set(mainFrameLeft, top, mainFrameRight, viewHeight - top)
        leftHandle.set(left, 0f, leftHandleRight, viewHeight.toFloat())

        val rightHandleLeft = mainFrame.right + halfStroke
        val rightHandleRight = rightHandleLeft + handleWidth
        rightHandle.set(rightHandleLeft, 0f, rightHandleRight, viewHeight.toFloat())
        updateShadows()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            drawRect(mainFrame, paintMainRect)
            drawRect(leftHandle, paintHandle)
            drawRect(rightHandle, paintHandle)
            drawRect(leftShadow, shadowPaint)
            drawRect(rightShadow, shadowPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val handled = when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                blockParent(false)
                onBeforeMove(event)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                onMove(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                blockParent(true)
                onFinishMove()
                true
            }
            else -> super.onTouchEvent(event)
        }
        if (handled) invalidate()
        return handled
    }


    private fun onMove(event: MotionEvent) {
        when (partToDraw) {
            Part.LEFT -> moveLeftHandle(event)

            Part.RIGHT -> moveRightHandle(event)

            Part.CENTER -> moveTogether(event)
        }
        if (partToDraw != Part.UNKNOWN) getFocusedRange()
    }

    private fun moveRightHandle(event: MotionEvent) {
        if (checkMinWidthRight(event.x)) {
            val newLeft = event.x
            rightHandle.offsetTo(newLeft, rightHandle.top)
            mainFrame.set(
                mainFrame.left,
                mainFrame.top,
                rightHandle.left - halfStroke,
                mainFrame.bottom
            )
            updateShadows()
        }
    }

    private fun checkMinWidthRight(x: Float): Boolean {
        val distanceToLeftEdge = x - leftHandle.left
        return distanceToLeftEdge > minFrameWidth
    }

    private fun checkMinWidthLeft(x: Float): Boolean {
        val distanceToRightEdge = rightHandle.right - x
        return distanceToRightEdge > minFrameWidth
    }

    private fun moveLeftHandle(event: MotionEvent) {
        if (checkMinWidthLeft(event.x)) {
            val newLeft = event.x
            leftHandle.offsetTo(newLeft, leftHandle.top)
            mainFrame.set(
                leftHandle.right + halfStroke,
                mainFrame.top,
                mainFrame.right,
                mainFrame.bottom
            )
            updateShadows()
        }
    }

    private fun updateShadows() {
        leftShadow.set(0f, 0f, leftHandle.left, viewHeight.toFloat())
        rightShadow.set(rightHandle.right, 0f, viewWidth.toFloat(), viewHeight.toFloat())
    }

    private fun moveTogether(event: MotionEvent) {
        if (event.inView()) {
            val newLeft = event.x - totalWidth() / 2
            val newTop = mainFrame.top
            mainFrame.offsetTo(newLeft, newTop)
            leftHandle.offsetTo(mainFrame.left - halfStroke - handleWidth, newTop - top)
            rightHandle.offsetTo(mainFrame.right + halfStroke, newTop - top)
            updateShadows()
        }
    }

    private fun onBeforeMove(event: MotionEvent) {
        partToDraw = when {
            isLeftHandle(event) -> Part.LEFT
            isRightHandle(event) -> Part.RIGHT
            isMainRect(event) -> Part.CENTER
            else -> Part.UNKNOWN
        }
    }

    private fun onFinishMove() {
        partToDraw = Part.UNKNOWN
    }

    private fun totalWidth(): Float {
        return mainFrame.right - mainFrame.left
    }

    private fun isRightHandle(event: MotionEvent): Boolean {
        return rightHandle.scale().contains(event.x, event.y)
    }

    private fun isLeftHandle(event: MotionEvent): Boolean {
        return leftHandle.scale().contains(event.x, event.y)
    }

    private fun isMainRect(event: MotionEvent): Boolean {
        return mainFrame.contains(event.x, event.y)
    }

    private var prevLeft = 0
    private var prevRight = 0
    private fun getFocusedRange() {
        val left = ((mainFrame.left / viewWidth) * 100).toInt()
        val right = ((mainFrame.right / viewWidth) * 100).toInt()

        if (prevLeft == left && prevRight == right) return

        prevLeft = left
        prevRight = right
        listeners.forEach { it.onFocusedRangeChanged(left, right) }
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    private fun MotionEvent.inView(): Boolean {
        val maxX = x + totalWidth() / 2
        val minX = x
        return maxX < viewWidth && minX > totalWidth() / 2
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

    override fun switchDayNightMode(nightMode: Boolean) {
        shadowPaint.color = resources.getColor(R.color.shadow, R.color.shadowNight, nightMode)
        invalidate()
    }

    private lateinit var scrollView: LockableScrollView


    private fun blockParent(unBlock: Boolean) {
        scrollView.setScrollingEnabled(unBlock)
    }
    fun setParent(scrollView: LockableScrollView) {
        this.scrollView = scrollView
    }

    interface Listener {
        fun onFocusedRangeChanged(left: Int, right: Int)
    }
}