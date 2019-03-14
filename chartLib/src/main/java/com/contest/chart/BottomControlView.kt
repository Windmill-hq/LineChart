package com.contest.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.contest.chart.model.LineChartData
import com.contest.chart.view.FocusWindow

class BottomControlView : View {


    private val paintBack = Paint().apply { color = Color.WHITE }
    private lateinit var window: FocusWindow
    private val smallLineChart = SmallLineChart()

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
        window = FocusWindow(context.resources.getColor(R.color.frameColor))
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(paintBack)
        smallLineChart.onDraw(canvas)
        window.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val w = measureDimension(desiredWidth, widthMeasureSpec)
        val h = measureDimension(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(w, h)
        window.setParentSize(w, h)
        smallLineChart.setParentSize(w, h)
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
        return result
    }


    fun addListener(listener: FocusWindow.Listener) {
        window.addListener(listener)
        window.addListener(smallLineChart)
    }

    fun setData(data: List<LineChartData>) {
        smallLineChart.init(data)
        window.callback()
        invalidate()
    }
}
