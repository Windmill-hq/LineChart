package com.contest.chart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.contest.chart.model.LineChartData
import com.contest.chart.view.BaseThemedChart
import com.contest.chart.view.FocusedRangeFrame

class TimeBasedLineChart : BaseThemedChart, FocusedRangeFrame.Listener {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var chartData: List<LineChartData>
    private var isInited = false
    private var viewWidth = 0
    private var viewHeight = 0

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInited) return

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
        invalidate() // todo do not call it here
    }
}
