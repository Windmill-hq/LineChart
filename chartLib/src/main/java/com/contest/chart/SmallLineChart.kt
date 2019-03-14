package com.contest.chart

import android.graphics.Canvas
import com.contest.chart.model.LineChartData
import com.contest.chart.view.FocusWindow

class SmallLineChart : FocusWindow.Listener {

    private lateinit var data: List<LineChartData>
    private var isInited = false
    private var parentWidth = 0
    private var parentHeight = 0


    fun onDraw(canvas: Canvas) {
        if (!isInited) return

        data.forEach {
            it.brokenLines.forEach { line ->
                line.draw(canvas, it.horizontalStep)
            }
        }
    }

    fun init(dataList: List<LineChartData>) {
        this.data = arrayListOf(dataList[0]) // todo leave only one

        data.forEach {
            it.brokenLines.forEach { line -> line.setYTo(parentHeight) }
        }

        data.forEach { it.setStep(parentWidth) }
        isInited = true
    }

    fun setParentSize(parentWidth: Int, parentHeight: Int) {
        this.parentWidth = parentWidth
        this.parentHeight = parentHeight
    }

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        data.forEach {
            it.brokenLines.forEach { line ->
                line.onFocusedWindowSizeChanged(left, right)
            }
        }
    }
}
