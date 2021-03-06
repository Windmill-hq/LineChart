package com.contest.chart.upper

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.contest.chart.Step
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.base.AbstractLineChart
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.model.LineChartData
import com.contest.chart.scale.Scale

class UpperChart : AbstractLineChart<UpperChartController>, ChartDetailsProvider,
    FocusedRangeFrame.Listener {
    private val scale = Scale(resources, this)
    private val step = Step(0f, 0f)
    private var internalInit = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onCreateController(data: LineChartData): UpperChartController {
        internalInit = true
        return UpperChartController(data, this)
    }

    override fun onAdditionalInit(chartData: LineChartData) {
        scale.setData(chartData)
    }

    override fun onMeasured(width: Int, height: Int) {
        super.onMeasured(width, height)
        scale.setSize(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        scale.draw(canvas)
        super.onDraw(canvas)
    }

    override fun getChartStep(): Step {
        step.xStep = getController().horizontalStep
        step.yStep = getController().verticalStep
        return step
    }

    override fun getPositionOffset(): Int {
        return getController().getFocusedRange().first
    }

    override fun getTotalHeight(): Int {
        return viewHeight
    }

    override fun onLineStateChanged(name: String, isEnabled: Boolean) {
        super.onLineStateChanged(name, isEnabled)
        scale.onLineStateChanged()
    }

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        if (!isInited) return
        chartController.onFocusedRangeChanged(left, right)
        if (internalInit) scale.onFocusedRangeChanged(getController().getFocusedRange())
    }

    override fun switchDayNightMode(nightMode: Boolean) {
        scale.switchDayNightMode(nightMode, resources)
    }

    fun setListener(listener: OnAnimationListener) {
        chartController.listener = listener
    }
}