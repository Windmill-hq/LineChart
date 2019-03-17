package com.contest.chart.upper

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import com.contest.chart.DataProvider
import com.contest.chart.R
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.base.MeasuredView
import com.contest.chart.model.InterceptionInfo

class DetailsView : MeasuredView, FocusedRangeFrame.Listener, UpperChart.UserClickListener {

    private val linePaint = Paint().apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val rectPaintStroke = Paint().apply {
        color = resources.getColor(R.color.detailsLineColor)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val rectPaintFill = Paint().apply {
        color = resources.getColor(R.color.backGround)
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    private val green = resources.getColor(R.color.green)
    private val red = resources.getColor(R.color.red)
    private val black = resources.getColor(R.color.black)

    private val detailsRect = RectF()

    private var rectTop = 5f
    private var rectWidth = 200f
    private var rectBottom = 155f
    private val radius = 10f
    private var positionX = 0f
    private var viewWidth = 0
    private var viewHeight = 0
    private val xOffset = 50
    private var nightMode = false

    private var dateLabel = "Sat, May 4"
    private var firstValue = 127
    private var firstLabel = "Joined"
    private var secondValue = 69
    private var secondLabel = "Left"
    private var inited = false

    private lateinit var dataProvider: DataProvider
    private val interceptorPrinter = InterceptorPrinter()


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(positionX, rectBottom, positionX, viewHeight.toFloat(), linePaint)
        drawDetails(canvas)
    }

    private fun drawDetails(canvas: Canvas) {
        if (!inited) return
        canvas.drawRoundRect(detailsRect, radius, radius, rectPaintFill)
        if (!nightMode) canvas.drawRoundRect(detailsRect, radius, radius, rectPaintStroke)
        drawData(canvas)
        interceptorPrinter.drawInterceptions(canvas)
    }

    private fun drawData(canvas: Canvas) {
        var x = detailsRect.left + 35
        var y = detailsRect.top + 35
        canvas.drawText(dateLabel, x, y, blackPaint())

        y += 50
        canvas.drawText(firstValue.toString(), x, y, greenPaint())
        y += 20
        canvas.drawText(firstLabel, x, y, greenPaint())

        x = detailsRect.left + 100
        y = detailsRect.top + 85
        canvas.drawText(secondValue.toString(), x, y, redPaint())

        y += 20
        canvas.drawText(secondLabel, x, y, redPaint())

    }

    private fun redPaint(): Paint {
        return textPaint.apply { color = red }
    }

    private fun greenPaint(): Paint {
        return textPaint.apply { color = green }
    }

    private fun blackPaint(): Paint {
        return textPaint.apply { color = black }
    }

//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        return when (event.action) {
////            MotionEvent.ACTION_DOWN,
////            MotionEvent.ACTION_MOVE,
//            MotionEvent.ACTION_DOWN -> {
//                moveTo(event.x, event.y)
//                true
//            }
//            else -> super.onTouchEvent(event)
//        }
//    }

    private var lastX = 0f

    override fun onClick(x: Float, offset: Int) {
        moveTo(x)
        requestData(x, offset)
        invalidate()
    }

    private fun moveTo(x: Float) {
        inited = true
        positionX = x
        if (isBlockInView(x)) {
            val left = positionX - xOffset
            val right = left + rectWidth
            detailsRect.set(left, rectTop, right, rectBottom)
        }
        lastX = x
    }

    private var positionOffset = 0
    private fun requestData(x: Float, offset: Int) {
        positionOffset = offset
        val interceptions = dataProvider.getInterceptions(x, positionOffset)
        interceptorPrinter.setData(interceptions)
    }

    private fun isBlockInView(x: Float): Boolean {
        return x > xOffset && x + rectWidth - xOffset < viewWidth
    }

    override fun onMeasured(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
        interceptorPrinter.conditionalY = height
    }

    override fun switchDayNightMode(nightMode: Boolean) {
        this.nightMode = nightMode
        val night = context.resources.getColor(R.color.detailsLineColorNight)
        val day = context.resources.getColor(R.color.detailsLineColor)
        linePaint.color = if (nightMode) night else day

        val nightDetail = context.resources.getColor(R.color.detailsBackgroundDark)
        val dayDetail = context.resources.getColor(R.color.backGround)
        rectPaintFill.color = if (nightMode) nightDetail else dayDetail
        interceptorPrinter.switchDayNightMode(nightMode, context.resources)
        invalidate()
    }

    fun setDataProvider(provider: DataProvider) {
        dataProvider = provider
    }

    override fun onFocusedRangeChanged(left: Int, right: Int) {
        requestData(lastX, positionOffset)
    }
}

class InterceptorPrinter {
    var conditionalY = 0
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val paintFilled = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val data = ArrayList<InterceptionInfo>()
    fun setData(data: List<InterceptionInfo>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun drawInterceptions(canvas: Canvas) {
        data.forEach {
            it.details.forEach { interception ->
                paint.color = Color.parseColor(interception.color)
                val center = interception.point
                canvas.drawCircle(center.x, center.y, 10f, paintFilled)
                canvas.drawCircle(center.x, center.y, 10f, paint)
            }
        }
    }


    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        val night = resources.getColor(R.color.backGroundDark)
        val day = resources.getColor(R.color.backGround)
        val color = if (nightMode) night else day
        paintFilled.color = color
    }
}

