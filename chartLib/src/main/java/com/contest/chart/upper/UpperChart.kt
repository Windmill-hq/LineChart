package com.contest.chart.upper

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.contest.chart.Step
import com.contest.chart.ChartDetailsProvider
import com.contest.chart.R
import com.contest.chart.base.AbstractLineChart
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getColor
import com.contest.chart.utils.getMaxValueInRange
import com.contest.chart.utils.toStringDate
import java.util.*
import kotlin.collections.ArrayList

class UpperChart : AbstractLineChart<UpperChartController>, ChartDetailsProvider {
    private val scale = Scale(resources)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onCreateController(data: LineChartData): UpperChartController {
        return UpperChartController(data, viewWidth, viewHeight, this)
    }

    override fun onAdditionalInit(chartData: LineChartData) {
        scale.setData(chartData)
        scale.setSize(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        scale.draw(canvas)
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
        scale.onFocusedRangeChanged(getController().getFocusedRange())
    }

    override fun switchDayNightMode(nightMode: Boolean) {
        scale.switchDayNightMode(nightMode, resources)
    }
}

class Scale(resources: Resources) : BaseScale<LineChartData>(resources) {

    private val horizontalScale = HorizontalScale(resources)
    private val verticalScale = VerticalScale(resources)
    override fun setData(data: LineChartData) {
        horizontalScale.setData(data.timeLine)
        verticalScale.setData(data.brokenLines)
    }

    override fun setSize(viewWidth: Int, viewHeight: Int) {
        horizontalScale.setSize(viewWidth, viewHeight)
        verticalScale.setSize(viewWidth, viewHeight)
    }

    override fun draw(canvas: Canvas) {
        horizontalScale.draw(canvas)
        verticalScale.draw(canvas)
    }

    override fun onFocusedRangeChanged(focusedRange: IntRange) {
        horizontalScale.onFocusedRangeChanged(focusedRange)
        verticalScale.onFocusedRangeChanged(focusedRange)
    }
}

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

class HorizontalScale(resources: Resources) : BaseScale<LongArray>(resources) {
    private lateinit var timeLine: Array<String>
    private val datesList = ArrayList<String>()
    private var inited = false
    var datesToDraw = 0
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
}


abstract class BaseScale<T>(resources: Resources) {
    protected val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.scaleText)
        textSize = 23f
    }

    abstract fun setData(data: T)
    abstract fun onFocusedRangeChanged(focusedRange: IntRange)
    abstract fun setSize(viewWidth: Int, viewHeight: Int)
    abstract fun draw(canvas: Canvas)
    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        paintText.color = resources.getColor(R.color.scaleText, R.color.scaleTextNight, nightMode)
    }
}

