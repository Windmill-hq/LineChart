package com.contest.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class BottomControlView : View {
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


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action: Int = MotionEventCompat.getActionMasked(event)
        val handled = when (action) {
            MotionEvent.ACTION_DOWN -> {
                window.onBeforeMove(event)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                window.onMove(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                window.onFinishMove()
                true
            }
            else -> super.onTouchEvent(event)
        }
        if (handled) invalidate()
        return handled
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

    public fun setListener(listener: Listener) {
        window.setListener(listener)
    }

    interface Listener {
        fun onWindowSizeChanged(left: Float, right: Float)
    }
}
