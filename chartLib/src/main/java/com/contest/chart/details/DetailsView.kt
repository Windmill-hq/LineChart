package com.contest.chart.details

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import com.contest.chart.DataProvider
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.base.MeasuredView

class DetailsView : MeasuredView, FocusedRangeFrame.Listener {

    private val interceptorPrinter = InterceptorPrinter()
    private val detailsWindow = DetailsWindow(resources)
    private val textBlock = TextBlock(resources)
    private lateinit var dataProvider: DataProvider

    private var lastX = 0f
    private var inited = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        if (!inited) return
        interceptorPrinter.draw(canvas)
        detailsWindow.draw(canvas)
        textBlock.draw(canvas, detailsWindow.left, detailsWindow.top)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_DOWN -> {
                handleEvent(event.x)
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun handleEvent(x: Float) {
        inited = true
        lastX = x
        detailsWindow.moveTo(x)
        requestInterceptionsAndInvalidate(x)
    }


    private fun requestInterceptionsAndInvalidate(x: Float) {
        val interception = dataProvider.getInterceptions(x)
        interceptorPrinter.setData(interception)
        textBlock.setData(interception)
        invalidate()
    }

    fun setDataProvider(provider: DataProvider) {
        dataProvider = provider
    }

    override fun onMeasured(width: Int, height: Int) {
        detailsWindow.setSize(width, height)
        interceptorPrinter.conditionalY = height
    }

    override fun switchDayNightMode(nightMode: Boolean) {
        detailsWindow.switchDayNightMode(nightMode, resources)
        interceptorPrinter.switchDayNightMode(nightMode, resources)
        invalidate()
    }

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        refresh()
    }

    fun refresh() {
        requestInterceptionsAndInvalidate(lastX)
    }
}