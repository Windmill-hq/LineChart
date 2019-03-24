package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.R
import com.contest.chart.model.BrokenLine
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getMaxValueInRange

class VerticalScale(resources: Resources, provider: ChartDetailsProvider) :
        BaseScale<ArrayList<BrokenLine>>(resources, provider) {

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

    private val textOffset = 3
    override fun draw(canvas: Canvas) {
        dataToDraw.forEachIndexed { index, value ->
            val startY = viewHeight - value * getStepY() - Constants.BOTTOM_VERTICAL_OFFSET
            canvas.drawLine(10f, startY, viewWidth, startY, paint)
            canvas.drawText(withSuffix(value), 10f, startY - textOffset, paintText)
        }
    }

    override fun onFocusedRangeChanged(focusedRange: IntRange) {
        super.onFocusedRangeChanged(focusedRange)
        val max = data.getMaxValueInRange(focusedRange, maxValuesStore)
        dataToDraw.clear()
        if (max != null) {
            maxY = max.toInt()
            defineData(maxY)
        }
    }

    private fun defineData(maxY: Int) {
        val step = maxY / Constants.AVERAGE_HORIZONTALS
        for (value in 0..maxY step step) {
            dataToDraw.add(value)
        }
    }

    override fun setSize(viewWidth: Int, viewHeight: Int) {
        this.viewWidth = viewWidth.toFloat()
        this.viewHeight = viewHeight.toFloat()
    }

    private fun withSuffix(count: Int): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c", count / Math.pow(1000.0, exp.toDouble()), "kMGTPE"[exp - 1])
    }
}