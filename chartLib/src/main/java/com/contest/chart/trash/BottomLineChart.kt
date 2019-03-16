package com.contest.chart.trash

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.contest.chart.model.LineChartData
import com.contest.chart.base.BaseThemedChart
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.base.Refresher

open class BottomLineChart : BaseThemedChart, FocusedRangeFrame.Listener, Refresher {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var isInited = false
    private var viewWidth = 0
    private var viewHeight = 0
    private val chartControllers = ArrayList<MultiLineChartController>()

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInited) return
        chartControllers.forEach { it.draw(canvas) }
    }

    fun setData(dataList: List<LineChartData>) {
        dataList.forEach {
            chartControllers.add(MultiLineChartController(it, viewWidth, viewHeight, this))
        }
        isInited = true
    }

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        if (!isInited) return
        chartControllers.forEach { it.onFocusedRangeChanged(left, right) }
    }

    fun onLineStateChanged(name: String, isEnabled: Boolean) {
        chartControllers.forEach { it.onLineStateChanged(name, isEnabled) }
    }

    override fun refresh() {
        invalidate()
    }
}
