package com.contest.chart.base

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.contest.chart.model.LineChartData

abstract class AbstractLineChart<CC : AbstractChartController<*>> : MeasuredView, FocusedRangeFrame.Listener,
    Refresher {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var isInited = false
    protected var viewWidth = 0
    protected var viewHeight = 0
    private lateinit var chartController: CC

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInited) return
        chartController.draw(canvas)
    }

    fun setData(chartData: LineChartData) {
        onAdditionalInit(chartData)
        chartController = onCreateController(chartData)
        isInited = true
    }

    open fun onAdditionalInit(chartData: LineChartData) {}

    abstract fun onCreateController(data: LineChartData): CC

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        if (!isInited) return
        chartController.onFocusedRangeChanged(left, right)
    }

    fun onLineStateChanged(name: String, isEnabled: Boolean) {
        chartController.onLineStateChanged(name, isEnabled)
    }

    override fun refresh() {
        invalidate()
    }

    fun getController(): CC {
        return chartController
    }

    override fun update() {
    }

    override fun switchDayNightMode(nightMode: Boolean) {}
}
