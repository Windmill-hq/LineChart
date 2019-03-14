package com.contest.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.contest.chart.model.LineChartData
import com.contest.chart.view.FocusedRangeFrame
import com.contest.chart.view.MeasuredView

class TimeBasedLineChart : MeasuredView, FocusedRangeFrame.Listener {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var chartData: List<LineChartData>
    private var isInited = false
    private var viewWidth = 0
    private var viewHeight = 0
    private val paintBackGround = Paint().apply { color = Color.WHITE }

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInited) return
        canvas.drawPaint(paintBackGround)

        chartData.forEach {
            it.brokenLines.forEach { line ->
                line.draw(canvas, it.xScale, it.yScale)
            }
        }
    }

    fun setData(dataList: List<LineChartData>) {
        this.chartData = dataList

        chartData.forEach {
            it.brokenLines.forEach { line -> line.setConditionalY(viewHeight) }
        }

        chartData.forEach { it.setCanvasSize(viewWidth, viewHeight) }
        isInited = true
    }


    override fun onFocusedRangeChanged(left: Int, right: Int) {
        if (!isInited) return
        chartData.forEach {
            it.brokenLines.forEach { line ->
                line.onFocusedRangeChanged(left, right)
            }
        }
        invalidate()
    }

    fun onLineStateChanged(name: String, isShow: Boolean) {
        chartData.forEach {
            it.brokenLines.forEach { line ->
                if (line.name == name) {
                    line.show(isShow)
                }
            }
            it.setScale()
        }
        invalidate()
    }

    fun switchNightMode(nightMode: Boolean) {
        val night = context.resources.getColor(R.color.backGroundDark)
        val day = context.resources.getColor(R.color.backGround)
        paintBackGround.color = if (nightMode) night else day
        invalidate()
    }
}
