package com.contest.chart.scale

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import com.contest.chart.R
import com.contest.chart.model.BrokenLine
import com.contest.chart.utils.getMaxValueInRange

class VerticalScale(resources: Resources) : BaseScale<ArrayList<BrokenLine>>(resources) {
    private lateinit var data: ArrayList<BrokenLine>
    private var viewHeight = 0f
    private var viewWidth = 0f
    private val maxValuesStore = ArrayList<Float>()
    private var maxY = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.scaleText)
    }

    private var step = 100
    override fun setData(data: ArrayList<BrokenLine>) {
        this.data = data
    }

    override fun draw(canvas: Canvas) {
        drawLines(canvas)

    }

    private fun drawLines(canvas: Canvas) {
        var y = viewHeight
        val count = viewHeight.toInt() / step
        for (i in 0 until count - 1) {
            y -= step
            canvas.drawLine(0f, y, viewWidth, y, paint)
            canvas.drawText(getText(y), 5f, y - 3, paintText)
        }
    }

    private fun getText(y: Float): String {
        val percent = (1 - y / viewHeight)
        return (percent * maxY).toInt().toString()
    }

    override fun onFocusedRangeChanged(focusedRange: IntRange) {
        val max = data.getMaxValueInRange(focusedRange, maxValuesStore)
        if (max != null) {
            maxY = max.toInt()
            step = viewHeight.toInt() / 6
        }
    }

    override fun setSize(viewWidth: Int, viewHeight: Int) {
        this.viewWidth = viewWidth.toFloat()
        this.viewHeight = viewHeight.toFloat()
    }
}