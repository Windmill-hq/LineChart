package com.contest.chart.base

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.contest.chart.model.LineChartData

abstract class AbstractLineChart<CC : AbstractChartController<*>> : MeasuredView, FocusedRangeFrame.Listener,
    ChartView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    protected var isInited = false
    protected var viewWidth = 0
    protected var viewHeight = 0
    protected lateinit var chartController: CC

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
        chartController.calculateSteps()
    }

    override fun getChartWidth(): Int {
        return viewWidth
    }

    override fun getChartHeight(): Int {
        return viewHeight
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInited) return
        chartController.draw(canvas)
    }

   open fun setData(chartData: LineChartData) {
        onAdditionalInit(chartData)
        chartController = onCreateController(chartData)
        isInited = true
        update()
    }

    open fun onAdditionalInit(chartData: LineChartData) {}

    abstract fun onCreateController(data: LineChartData): CC

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        if (!isInited) return
        chartController.onFocusedRangeChanged(left, right)
    }

    open fun onLineStateChanged(name: String, isEnabled: Boolean) {
        chartController.onLineStateChanged(name, isEnabled)
    }

    override fun update() {
        invalidate()
    }

    fun getController(): CC {
        return chartController
    }

    override fun switchDayNightMode(nightMode: Boolean) {}
}
