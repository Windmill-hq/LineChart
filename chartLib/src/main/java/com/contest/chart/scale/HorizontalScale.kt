package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.utils.Constants
import com.contest.chart.utils.toStringDate

class HorizontalScale(resources: Resources, provider: ChartDetailsProvider)
    : BaseScale<LongArray>(resources, provider) {

    private lateinit var timeLine: Array<String>
    private val datesList = ArrayList<String>()
    private var inited = false
    private var datesToDraw = 0
    var y = 0f
    var newStep = 0f


    override fun setData(data: LongArray) {
        this.timeLine = data.toStringDate()
    }

    override fun draw(canvas: Canvas) {
        if (!inited) return

        var x: Float

        datesList.forEachIndexed { index, date ->
            x = index * newStep
            canvas.drawText(date, x, y, paintText)
        }
    }


    override fun setSize(viewWidth: Int, viewHeight: Int) {
        y = viewHeight.toFloat() - 6
        datesToDraw = (viewWidth / Constants.HORIZONTAL_STEP).toInt()
        newStep = (viewWidth / datesToDraw).toFloat()
    }


    override fun onFocusedRangeChanged(focusedRange: IntRange) {
        inited = true
        datesList.clear()
        defineDataToDraw(focusedRange)
    }

    private fun defineDataToDraw(range: IntRange) {
        val count = range.last - range.first
        val step = count / datesToDraw
        for (index in range step step) {
            datesList.add(timeLine[index])
        }
    }

    override fun onLineStateChanged() {

    }
}