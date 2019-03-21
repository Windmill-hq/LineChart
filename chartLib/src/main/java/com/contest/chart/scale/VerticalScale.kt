package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.R
import com.contest.chart.model.BrokenLine
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getMaxValueInRange

class VerticalScale(resources: Resources, provider: ChartDetailsProvider)
    : BaseScale<ArrayList<BrokenLine>>(resources, provider) {

    private lateinit var data: ArrayList<BrokenLine>
    private var viewHeight = 0f
    private var viewWidth = 0f
    private val maxValuesStore = ArrayList<Float>()
    private var maxY = 0
    private val dataToDraw = ArrayList<Int>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.scaleText)
    }

    override fun setData(data: ArrayList<BrokenLine>) {
        this.data = data
    }

    override fun draw(canvas: Canvas) {
        dataToDraw.forEachIndexed { index, value ->
            val startY = viewHeight - value * getStepY()
            canvas.drawLine(0f, startY, viewWidth, startY, paint)
            if (index != 0) canvas.drawText(value.toString(), 5f, startY - 3, paintText)
        }
    }

    override fun onFocusedRangeChanged(focusedRange: IntRange) {
        super.onFocusedRangeChanged(focusedRange)
        val max = data.getMaxValueInRange(focusedRange, maxValuesStore)
        if (max != null) {
            maxY = max.toInt()
            defineData(maxY)
        }
    }

    private fun defineData(maxY: Int) {
        dataToDraw.clear()
        val step = maxY / Constants.AVERAGE_HORIZONTALS
        for (value in 0..maxY step step) {
            dataToDraw.add(value)
        }
    }

    override fun setSize(viewWidth: Int, viewHeight: Int) {
        this.viewWidth = viewWidth.toFloat()
        this.viewHeight = viewHeight.toFloat()
    }
}