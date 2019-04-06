package com.contest.chart.upper

import android.animation.Animator
import android.animation.ValueAnimator
import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.BaseListener
import com.contest.chart.base.ChartView
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.animateValue
import com.contest.chart.utils.getChartMaxSize
import com.contest.chart.utils.getChartMaxValueInRange
import java.util.concurrent.atomic.AtomicBoolean

class UpperChartController(
    chartData: LineChartData,
    chartView: ChartView
) : AbstractChartController<UpperChatLinePrinter>(chartData, chartView) {

    private var focusRange = 0..1
    private val isHorizontalAnimBusy = AtomicBoolean()
    private val needUpdateHorizontalStep = AtomicBoolean()

    private val horizontalAnimListener = object : BaseListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            horizontalStep = animation.animatedValue as Float
            chartView.update()
        }

        override fun onAnimationEnd(animation: Animator) {
            isHorizontalAnimBusy.set(false)
            if (needUpdateHorizontalStep.get()) {
                needUpdateHorizontalStep.set(false)
                calculateHorizontalStep()
            }
        }
    }

    override fun onCreateLinePainter(line: BrokenLine): UpperChatLinePrinter {
        return UpperChatLinePrinter(line, this, Constants.UPPER_CHART_LINE_THICKNESS)
    }

    private fun notifyFocusRangeChanged() {
        getControllers().forEach {
            it.offsetChanged(focusRange.first)
        }
    }

    override fun getMaxValue(): Float {
        return chartData.getChartMaxValueInRange(focusRange, yStepStore)
    }

    override fun getMaxSize(): Int {
        return chartData.getChartMaxSize(xStepStore, focusRange)
    }

    override fun calculateSteps() {
        calculateHorizontalStepNoAnim()
        calculateVerticalStep()
    }


    private fun calculateHorizontalStep() {
        if (isHorizontalAnimBusy.get()) {
            needUpdateHorizontalStep.set(true)
        } else {
            val maxVal = getMaxSize()
            if (maxVal == 0) return
            val newStep = view.getChartWidth() / maxVal.toFloat()
            if (horizontalStep != newStep) {
                isHorizontalAnimBusy.set(true)
                animateValue(horizontalStep, newStep, 200, horizontalAnimListener)
            }
        }
    }

    fun onFocusedRangeChanged(left: Int, right: Int) {
        val size = lineControllers[0].getPoints().size
        val focusLeft = size * left / 100
        val focusRight = size * right / 100
        focusRange = focusLeft..focusRight
        calculateSteps()
        notifyFocusRangeChanged()
        view.update()
    }

    override fun getFocusedRange(): IntRange {
        return focusRange
    }
}


