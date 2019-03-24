package com.contest.chart.bottom

import android.animation.Animator
import android.animation.ValueAnimator
import com.contest.chart.base.AbstractChartController
import com.contest.chart.base.BaseListener
import com.contest.chart.base.LinePrinter
import com.contest.chart.base.Refresher
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getChartMaxSize
import com.contest.chart.utils.getChartMaxValue
import java.util.concurrent.atomic.AtomicBoolean

class BottomChartController(chartData: LineChartData, width: Int, height: Int, refresher: Refresher) :
        AbstractChartController<LinePrinter>(chartData, width, height, refresher) {

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

    private fun calculateVerticalStep() {
        if (isVerticalAnimBusy.get()) {
            needUpdateVerticalStep.set(true)
        } else {
            val maxVal = getMaxValue()
            if (maxVal == 0f) return
            val newStep = (height - Constants.UPPER_VERTICAL_OFFSET) / maxVal
            if (verticalStep != newStep) {
                isVerticalAnimBusy.set(true)
                ValueAnimator.ofFloat(verticalStep, newStep).apply {
                    duration = 400
                    repeatCount = 0
                    addUpdateListener(verticalAnimListener)
                    addListener(verticalAnimListener)
                }.start()
            }
        }
    }


    override fun onCreateLinePainter(line: BrokenLine): LinePrinter {
        return LinePrinter(line, this, Constants.BOTTOM_CHART_LINE_THICKNESS)
    }

    override fun getMaxValue(): Float {
        return chartData.getChartMaxValue(yStepStore)
    }

    override fun getMaxSize(): Int {
        return chartData.getChartMaxSize(xStepStore)
    }

    override fun notifyFocusRangeChanged() {
    }

    override fun calculateSteps() {
        calculateVerticalStep()
        calculateHorizontalStepNoAnim()
    }
}