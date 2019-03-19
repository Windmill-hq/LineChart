package com.contest.chart.base

import android.graphics.Canvas
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractChartController<LC : BaseLinePainter>(
        val chartData: LineChartData,
        private val width: Int,
        private val height: Int,
        private val refresher: Refresher
) : Focus {

    private val lineControllers = ArrayList<LC>()
    var xStep = 0f
    var yStep = 0f
    protected var focusRange = 0..1
    private val isBusy = AtomicBoolean()
    protected var yStepStore: ArrayList<Float> = ArrayList()
    protected var xStepStore: ArrayList<Int> = ArrayList()

    init {
        chartData.brokenLines.forEach { lineControllers.add(onCreateLinePainter(it, height)) }
        calculateScales()
    }

    abstract fun onCreateLinePainter(line: BrokenLine, conditionalY: Int): LC

    fun draw(canvas: Canvas) {
        lineControllers.forEach {
            it.draw(canvas, xStep, yStep)
        }
    }

    private fun calculateScales() {
        calculateXStep()
        calculateYScale()
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

    private fun calculateYScale() {
//        if (isBusy.get()) return

        val maxVal = getMaxValue()
        yStep = (height - Constants.SPARE_SPACE_Y) / maxVal
    }
}


