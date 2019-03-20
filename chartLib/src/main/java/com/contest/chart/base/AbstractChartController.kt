package com.contest.chart.base

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData

abstract class AbstractChartController<LC : BaseLinePainter>(
        val chartData: LineChartData,
        protected val width: Int,
        protected val height: Int,
        private val refresher: Refresher
) : Focus {

    private val lineControllers = ArrayList<LC>()
    var horizontalStep = 0f
    var verticalStep = 0f
    protected var focusRange = 0..1
    protected var yStepStore: ArrayList<Float> = ArrayList()
    protected var xStepStore: ArrayList<Int> = ArrayList()

    init {
        chartData.brokenLines.forEach { lineControllers.add(onCreateLinePainter(it, height)) }
    }

    abstract fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): LC

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
}

interface BaseListener : Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    override fun onAnimationRepeat(animation: Animator?) {
    }


    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }
}


