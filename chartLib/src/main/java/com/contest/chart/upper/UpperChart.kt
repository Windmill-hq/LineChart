package com.contest.chart.upper

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.contest.chart.Step
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.base.AbstractLineChart
import com.contest.chart.model.LineChartData
import com.contest.chart.scale.Scale

class UpperChart : AbstractLineChart<UpperChartController>, ChartDetailsProvider {
    private val scale = Scale(resources, this)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onCreateController(data: LineChartData): UpperChartController {
        return UpperChartController(data, viewWidth, viewHeight, this)
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
        return Step(getController().horizontalStep, getController().verticalStep)
    }

    override fun getPositionOffset(): Int {
        return getController().getFocusedRange().first
    }

    override fun getTotalHeight(): Int {
        return viewHeight
    }

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        super.onFocusedRangeChanged(left, right)
        scale.onFocusedRangeChanged(getController().getFocusedRange())
    }

    override fun switchDayNightMode(nightMode: Boolean) {
        scale.switchDayNightMode(nightMode, resources)
    }
}