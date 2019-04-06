package com.contest.chart.details

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.R
import com.contest.chart.model.InterceptionInfo
import com.contest.chart.utils.Constants
import com.contest.chart.utils.getColor

class InterceptorPrinter {
    var conditionalY = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = Constants.UPPER_CHART_LINE_THICKNESS
    }

    private val paintFilled = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private lateinit var interceptionInfo: InterceptionInfo

    fun setData(intercept: InterceptionInfo) {
        interceptionInfo = intercept
    }

    fun getSize(): Int {
        return interceptionInfo.details.size
    }

    fun draw(canvas: Canvas) {
        interceptionInfo.details.forEach { interception ->
            paint.color = Color.parseColor(interception.color)
            val center = interception.point
            canvas.drawCircle(center.x, center.y, Constants.CIRCLE_RADIUS, paintFilled)
            canvas.drawCircle(center.x, center.y, Constants.CIRCLE_RADIUS, paint)
        }
    }

    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        paintFilled.color =
            resources.getColor(R.color.backGround, R.color.backGroundDark, nightMode)
    }
}