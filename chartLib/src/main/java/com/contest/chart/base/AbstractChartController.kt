package com.contest.chart.base

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.upper.OnAnimationListener
import com.contest.chart.utils.Constants
import com.contest.chart.utils.animateValue
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractChartController<LP : BaseLinePrinter>(
    val chartData: LineChartData,
    protected val view: ChartView
) : DetalsProvider {
    var listener: OnAnimationListener? = null

    protected val lineControllers = ArrayList<LP>()
    var horizontalStep = 0f
    var verticalStep = 0f
    protected var yStepStore: ArrayList<Float> = ArrayList()
    protected var xStepStore: ArrayList<Int> = ArrayList()

    init {
        chartData.brokenLines.forEach { lineControllers.add(onCreateLinePainter(it)) }
    }

    private val isVerticalAnimBusy = AtomicBoolean()
    private val needUpdateVerticalStep = AtomicBoolean()

    private val verticalAnimListener = object : BaseListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            verticalStep = animation.animatedValue as Float
            view.update()
        }

        override fun onAnimationEnd(animation: Animator) {
            isVerticalAnimBusy.set(false)
            if (needUpdateVerticalStep.get()) {
                needUpdateVerticalStep.set(false)
                calculateVerticalStep()
            }
            listener?.onAnimEnd()
        }
    }

    protected fun calculateVerticalStep() {
        if (isVerticalAnimBusy.get()) {
            needUpdateVerticalStep.set(true)
        } else {
            val maxVal = getMaxValue()
            if (maxVal == 0f) return
            val newStep = (view.getChartHeight() - Constants.UPPER_VERTICAL_OFFSET) / maxVal
            if (verticalStep != newStep) {
                isVerticalAnimBusy.set(true)
                animateValue(verticalStep, newStep, 200, verticalAnimListener)
            }
        }
    }


    override fun getStartY(): Int {
        return view.getChartHeight()
    }

    abstract fun onCreateLinePainter(line: BrokenLine): LP

    abstract fun getMaxSize(): Int

    abstract fun getMaxValue(): Float

    abstract fun calculateSteps()

    fun draw(canvas: Canvas) {
        lineControllers.forEach {
            it.draw(canvas, horizontalStep, verticalStep)
        }
    }

    fun onLineStateChanged(name: String, isShow: Boolean) {
        chartData.brokenLines.forEach { if (it.name == name) it.isEnabled = isShow }
        calculateSteps()
        view.update()
    }

    fun getControllers(): List<LP> {
        return lineControllers
    }

    protected fun calculateHorizontalStepNoAnim() {
        val maxSize = getMaxSize()
        if (maxSize == 0) return
        horizontalStep = view.getChartWidth() / maxSize.toFloat()
    }
}

interface BaseListener : Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    override fun onAnimationRepeat(animation: Animator?) {
    }


    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }
}


