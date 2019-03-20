package com.contest.chart.base

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.util.Log
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractChartController<LC : BaseLinePainter>(
    val chartData: LineChartData,
    private val width: Int,
    private val height: Int,
    private val refresher: Refresher
) : Focus, SimpleListener, ValueAnimator.AnimatorUpdateListener {

    private val lineControllers = ArrayList<LC>()
    var xStep = 0f
    var yStep = 0f
    protected var focusRange = 0..1
    private val isYAnimBusy = AtomicBoolean()
    private val updateAgain = AtomicBoolean()
    protected var yStepStore: ArrayList<Float> = ArrayList()
    protected var xStepStore: ArrayList<Int> = ArrayList()
    private var scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var scheduledFuture: Future<*>
    private var isSchedularStarted = false

    init {
        chartData.brokenLines.forEach { lineControllers.add(onCreateLinePainter(it, height)) }
        schedulerUpdater()
    }

    abstract fun schedulerUpdater()

    protected fun runUpdater() {
        if (isSchedularStarted) return

        scheduledFuture = scheduler.scheduleAtFixedRate({ refresher.update() }, 0, 50, TimeUnit.MILLISECONDS)
        Log.d("SCHEDULER", " SCHEDULER  started")

    }

    private fun stopUpdater() {
        if (!scheduledFuture.isCancelled && !scheduledFuture.isDone) {
            scheduledFuture.cancel(true)
            Log.d("SCHEDULER", " SCHEDULER  stopped")
            isSchedularStarted = false
        } else {
            Log.d("SCHEDULER", "tried to stop SCHEDULER  when it already stopped")
        }
    }

    abstract fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): LC

    fun draw(canvas: Canvas) {
        lineControllers.forEach {
            it.draw(canvas, xStep, yStep)
        }
    }

    private fun calculateScales() {
        calculateXStep()
        calculateYStep()
    }

    fun onFocusedRangeChanged(left: Int, right: Int) {
        val size = lineControllers[0].getPoints().size
        val focusLeft = size * left / 100
        val focusRight = size * right / 100
        focusRange = focusLeft..focusRight
        calculateScales()
        notifyFocusRangeChanged()
        refresher.refresh()
    }

    abstract fun notifyFocusRangeChanged()

    fun onLineStateChanged(name: String, isShow: Boolean) {
        chartData.brokenLines.forEach { line ->
            if (line.name == name) line.isEnabled = isShow
        }
        calculateScales()
        refresher.refresh() // todo need hide/ show line with animation
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

    abstract fun getMaxSize(): Int

    abstract fun getMaxValue(): Float

    private fun calculateXStep() {
        val maxSize = getMaxSize()
        xStep = (width - Constants.SPARE_SPACE_X) / maxSize.toFloat()
    }

    abstract fun isAnimationEnabled(): Boolean

    private var firstLaunch = true

    private fun calculateYStep() {
        if (firstLaunch || !isAnimationEnabled()) {
            firstLaunch = false
            calculateYStepWithoutAnim()
        } else if (isAnimationEnabled()) {
            calculateYStepWithAnim()
        }
    }

    private fun calculateYStepWithoutAnim() {
        val maxVal = getMaxValue()
        yStep = (height - Constants.SPARE_SPACE_Y) / maxVal
    }

    private fun calculateYStepWithAnim() {
        if (isYAnimBusy.get()) {
            updateAgain.set(true)
        } else {
            val maxVal = getMaxValue()
            val newStep = (height - Constants.SPARE_SPACE_Y) / maxVal
            if (yStep != newStep) {
                isYAnimBusy.set(true)
                ValueAnimator.ofFloat(yStep, newStep).apply {
                    duration = 400
                    repeatCount = 0
                    addUpdateListener(this@AbstractChartController)
                    addListener(this@AbstractChartController)
                }.start()
            }
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        yStep = animation.animatedValue as Float
    }

    override fun onAnimationEnd(animation: Animator) {
        isYAnimBusy.set(false)
        if (updateAgain.get()) {
            updateAgain.set(false)
            calculateYStepWithAnim()
        }
    }

}

interface SimpleListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
    }


    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }
}


