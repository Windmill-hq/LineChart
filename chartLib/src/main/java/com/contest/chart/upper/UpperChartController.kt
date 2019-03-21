package com.contest.chart.upper

import android.animation.Animator
import android.animation.ValueAnimator
import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.BaseListener
import com.contest.chart.base.Refresher
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getChartMaxSize
import com.contest.chart.utils.getChartMaxValueInRange
import java.util.concurrent.atomic.AtomicBoolean

class UpperChartController(
        chartData: LineChartData,
        width: Int,
        height: Int,
        refresher: Refresher
) : AbstractChartController<UpperChatLinePrinter>(chartData, width, height, refresher) {

    private val isVerticalAnimBusy = AtomicBoolean()
    private val needUpdateVerticalStep = AtomicBoolean()

    private val verticalAnimListener = object : BaseListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            verticalStep = animation.animatedValue as Float
            refresher.refresh()
        }

        override fun onAnimationEnd(animation: Animator) {
            isVerticalAnimBusy.set(false)
            if (needUpdateVerticalStep.get()) {
                needUpdateVerticalStep.set(false)
                calculateVerticalStep()
            }
        }
    }

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
            refresher.refresh()
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
        calculateVerticalStep()
        calculateHorizontalStep()
    }


    private fun calculateVerticalStep() {
        if (isVerticalAnimBusy.get()) {
            needUpdateVerticalStep.set(true)
        } else {
            val maxVal = getMaxValue()
            if (maxVal == 0f) return
            val newStep = (height - Constants.SPARE_VERTICAL_SPACE) / maxVal
            if (verticalStep != newStep) {
                isVerticalAnimBusy.set(true) // TODO NEED CHECK AGAIN MAY BE ALREADY IN PROGRESS?
                ValueAnimator.ofFloat(verticalStep, newStep).apply {
                    duration = 400
                    repeatCount = 0
                    addUpdateListener(verticalAnimListener)
                    addListener(verticalAnimListener)
                }.start()
            }
        }
    }

    private fun calculateHorizontalStep() {
        if (isHorizontalAnimBusy.get()) {
            needUpdateHorizontalStep.set(true)
        } else {
            val maxVal = getMaxSize()
            if (maxVal == 0) return
            val newStep = width / maxVal
            if (horizontalStep != newStep.toFloat()) {
                isHorizontalAnimBusy.set(true)
                ValueAnimator.ofFloat(horizontalStep, newStep.toFloat()).apply {
                    duration = 200
                    repeatCount = 0
                    addUpdateListener(horizontalAnimListener)
                    addListener(horizontalAnimListener)
                }.start()
            }
        }
    }
}

