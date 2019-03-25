package com.contest.chart.base

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.animateValue
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractChartController<LC : BaseLinePrinter>(
    val chartData: LineChartData,
    protected var width: Int,
    protected var height: Int,
    private val refresher: Refresher
) : DetalsProvider {

    private val lineControllers = ArrayList<LC>()
    var horizontalStep = 0f
    var verticalStep = 0f
    protected var focusRange = 0..1
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

    protected fun calculateVerticalStep() {
        if (isVerticalAnimBusy.get()) {
            needUpdateVerticalStep.set(true)
        } else {
            val maxVal = getMaxValue()
            if (maxVal == 0f) return
            val newStep = (height - Constants.UPPER_VERTICAL_OFFSET) / maxVal
            if (verticalStep != newStep) {
                isVerticalAnimBusy.set(true)
                animateValue(verticalStep, newStep, 400, verticalAnimListener)
            }
        }
    }


    override fun getStartY(): Int {
        return height
    }

    abstract fun onCreateLinePainter(line: BrokenLine): LC

    abstract fun getMaxSize(): Int

    abstract fun getMaxValue(): Float

    abstract fun notifyFocusRangeChanged()

    abstract fun calculateSteps()

    fun draw(canvas: Canvas) {
        lineControllers.forEach {
            it.draw(canvas, horizontalStep, verticalStep)
        }
    }

    fun onFocusedRangeChanged(left: Int, right: Int) {
        val size = lineControllers[0].getPoints().size
        val focusLeft = size * left / 100
        val focusRight = size * right / 100
        focusRange = focusLeft..focusRight
        calculateSteps()
        notifyFocusRangeChanged()
        refresher.refresh()
    }

    fun onLineStateChanged(name: String, isShow: Boolean) {
        chartData.brokenLines.forEach { if (it.name == name) it.isEnabled = isShow }
        calculateSteps()
        refresher.refresh()
    }

    override fun isFocused(pos: Int): Boolean {
        return pos in focusRange
    }

    override fun getFocusedRange(): IntRange {
        return focusRange
    }

    fun getControllers(): List<LC> {
        return lineControllers
    }

    protected fun calculateVerticalStepNoAnim() {
        val maxVal = getMaxValue()
        verticalStep = (height - Constants.UPPER_VERTICAL_OFFSET) / maxVal
    }

    protected fun calculateHorizontalStepNoAnim() {
        val maxSize = getMaxSize()
        if (maxSize == 0) return
        horizontalStep = width / maxSize.toFloat()
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
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


