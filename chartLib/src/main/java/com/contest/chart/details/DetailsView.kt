package com.contest.chart.details

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import com.contest.chart.DataProvider
import com.contest.chart.LockableScrollView
import com.contest.chart.base.MeasuredView

class DetailsView : MeasuredView {

    private val interceptorPrinter = InterceptorPrinter()
    private val detailsWindow = DetailsWindow(resources)
    private val textBlock = TextBlock()
    private lateinit var dataProvider: DataProvider

    private var lastX = 0f
    private var inited = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        if (!inited) return
        interceptorPrinter.draw(canvas)
        detailsWindow.draw(canvas)
        textBlock.draw(canvas, detailsWindow.left, detailsWindow.top)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                blockParent(false)
                handleEvent(event.x)
            }
            MotionEvent.ACTION_MOVE -> {
                handleEvent(event.x)
            }

            MotionEvent.ACTION_UP -> {
                blockParent(true)
                inited = false
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                blockParent(true)
                inited = false
                invalidate()
            }
        }
        return true
    }

    private fun blockParent(unBlock: Boolean) {
//        scrollView.setScrollingEnabled(unBlock)
    }

    private fun handleEvent(x: Float) {
        inited = true
        lastX = x
        requestInterceptionsAndInvalidate(x)
        detailsWindow.moveTo(x, interceptorPrinter.getSize())
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
        textBlock.switchDayNightMode(nightMode, resources)
        invalidate()
    }

    fun refresh() {
        requestInterceptionsAndInvalidate(lastX)
    }

    private lateinit var scrollView: LockableScrollView

    fun setParent(scrollView: LockableScrollView) {
        this.scrollView = scrollView
    }
}