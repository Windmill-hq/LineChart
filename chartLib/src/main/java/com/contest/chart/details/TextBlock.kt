package com.contest.chart.details

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.contest.chart.R
import com.contest.chart.model.InterceptionInfo
import java.util.*

class TextBlock {
    private lateinit var interception: InterceptionInfo

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 36f
    }

    private val numberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 36f
    }

    private val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 30f
    }

    private val digitWidth = 20f
    fun draw(canvas: Canvas, left: Float, top: Float) {
        val x = left + 15
        var y = top + 35
        val dateLabel = Date(interception.timeLabel).toString().substring(0..10)
        canvas.drawText(dateLabel, x, y, datePaint)

        y += 40

        interception.details.forEachIndexed { index, data ->
            val lineY = (index * 40 + y)
            val valueLenght = data.value.length() + 1
            canvas.drawText(data.value.toInt().toString(), x, lineY, numberPaint(data.color))
            canvas.drawText(data.name, x + (valueLenght * digitWidth), lineY, textPaint(data.color))
        }
    }

    private fun numberPaint(col: String): Paint {
        return numberPaint.apply { color = Color.parseColor(col) }
    }


    private fun textPaint(col: String): Paint {
        return textPaint.apply { color = Color.parseColor(col) }
    }


    fun setData(interception: InterceptionInfo) {
        this.interception = interception
    }

    fun switchDayNightMode(nightMode: Boolean, resources: Resources) {
        val night = resources.getColor(R.color.backGround)
        val day = resources.getColor(R.color.black)
        val color = if (nightMode) night else day
        datePaint.color = color
    }
}

fun Float.length() = when (this) {
    0f -> 1
    else -> Math.log10(Math.abs(toDouble())).toInt() + 1
}
