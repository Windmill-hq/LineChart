package com.contest.chart.upper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.contest.chart.Step
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.base.AbstractLineChart
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import java.util.*
import kotlin.collections.ArrayList

class UpperChart : AbstractLineChart<UpperChartController>, ChartDetailsProvider {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val horizontalScale = HorizontalScale()

    override fun onCreateController(data: LineChartData): UpperChartController {
        horizontalScale.setTimeLine(data.timeLine)
        horizontalScale.setSize(viewWidth, viewHeight)
        return UpperChartController(data, viewWidth, viewHeight, this)
    }

    override fun onDraw(canvas: Canvas) {
        horizontalScale.draw(canvas)
        super.onDraw(canvas)
    }

    override fun getChartStep(): Step {
        return Step(getController().xStep, getController().yStep)
    }

    override fun getPositionOffset(): Int {
        return getController().getFocusedRange().first
    }

    override fun getTotalHeight(): Int {
        return viewHeight
    }

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        super.onFocusedRangeChanged(left, right)
        horizontalScale.onFocusedRangeChanged(getController().getFocusedRange())
    }
}

class HorizontalScale : BaseScale() {
    override fun populateDataToDraw(range: IntRange) {
        val count = range.last - range.first
        val step = count / datesToDraw
        for (index in range step step) {
            datesList.add(timeLine[index])
        }
    }

    override fun draw(canvas: Canvas) {
        if (!inited) return

        var x: Float

        datesList.forEachIndexed { index, date ->
            x = index * newStep
            canvas.drawText(date, x, y, paint)
        }
    }
}

abstract class BaseScale {

    protected lateinit var timeLine: Array<String>
    protected val datesList = ArrayList<String>()
    protected var inited = false
    var datesToDraw = 0
    var y = 0f

    fun setTimeLine(timeLine: LongArray) {
        this.timeLine = timeLine.toStringDate()
    }

    protected val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 23f
        color = Color.GRAY
    }

    fun onFocusedRangeChanged(range: IntRange) {
        inited = true
        datesList.clear()
        populateDataToDraw(range)
    }

    abstract fun populateDataToDraw(range: IntRange)

    abstract fun draw(canvas: Canvas)

    fun setSize(viewWidth: Int, viewHeight: Int) {
        y = viewHeight.toFloat() - 6
        datesToDraw = (viewWidth / Constants.HORIZONTAL_STEP).toInt()
        newStep = (viewWidth / datesToDraw).toFloat()
    }

    var newStep = 0f
}

private fun LongArray.toStringDate(): Array<String> {
    val dateStr = Array(this.size) { "" }

    this.forEachIndexed { index, date ->
        dateStr[index] = Date(date).toString().substring(4, 10)
    }
    return dateStr
}
