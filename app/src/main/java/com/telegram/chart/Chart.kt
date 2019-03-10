package com.telegram.chart

import android.content.Context
import android.graphics.*
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class Chart : View {
    val DEBUG_TAG = "Chart gesture"

    private val paintBack = Paint().apply { color = Color.CYAN }
    private val window = ActiveWindow()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        //todo
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(paintBack)
        window.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val w = measureDimension(desiredWidth, widthMeasureSpec)
        val h = measureDimension(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(w, h)
        window.setParentSize(w, h)
    }

    var isDown = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action: Int = MotionEventCompat.getActionMasked(event)
        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                isDown = true
                Log.d(DEBUG_TAG, "Action was DOWN")
                true
            }
            MotionEvent.ACTION_MOVE -> {
                moveWindow(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                isDown = false
                Log.d(DEBUG_TAG, "Action was UP")
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun moveWindow(event: MotionEvent) {
        window.onMove(event)
        invalidate()
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }

        if (result < desiredSize) {
            Log.e("ChartView", "The view is too small, the content might get cut")
        }
        return result
    }
}
