package com.contest.chart.upper

import android.animation.Animator
import android.animation.ValueAnimator
import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.BaseListener
import com.contest.chart.base.Refresher
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.animateValue
import com.contest.chart.utils.getChartMaxSize
import com.contest.chart.utils.getChartMaxValueInRange
import java.util.concurrent.atomic.AtomicBoolean

class UpperChartController(
    chartData: LineChartData,
    width: Int,
    height: Int,
    refresher: Refresher
) : AbstractChartController<UpperChatLinePrinter>(chartData, width, height, refresher) {

    private val isHorizontalAnimBusy = AtomicBoolean()
    private val needUpdateHorizontalStep = AtomicBoolean()

    private val horizontalAnimListener = object : BaseListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            horizontalStep = animation.animatedValue as Float
            refresher.refresh()
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

    override fun notifyFocusRangeChanged() {
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
            val newStep = width / maxVal.toFloat()
            if (horizontalStep != newStep) {
                isHorizontalAnimBusy.set(true)
                animateValue(horizontalStep, newStep, 200, horizontalAnimListener)
            }
        }
    }
}


