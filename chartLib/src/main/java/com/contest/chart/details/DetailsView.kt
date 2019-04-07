package com.contest.chart.details

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import com.contest.chart.DataProvider
import com.contest.chart.LockableScrollView
import com.contest.chart.base.FrameChangeListener
import com.contest.chart.base.MeasuredView
import com.contest.chart.upper.OnAnimationListener

class DetailsView : MeasuredView, FrameChangeListener, OnAnimationListener {


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

    override fun onFrameChanged() {
        if (inited) handleEvent(lastX)
    }

    override fun onAnimEnd() {
        onFrameChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleEvent(event.x)
            }
            MotionEvent.ACTION_MOVE -> {
//                blockParent(false)
                handleEvent(event.x)
            }

            MotionEvent.ACTION_UP -> {
//                inited = false
//                blockParent(true)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
//                inited = false
                invalidate()
            }
        }
        return true
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
        handleEvent(lastX)
    }

    private lateinit var scrollView: LockableScrollView

    private fun blockParent(unBlock: Boolean) {
        scrollView.scrollable = unBlock
    }

    fun setParent(scrollView: LockableScrollView) {
        this.scrollView = scrollView
    }
}