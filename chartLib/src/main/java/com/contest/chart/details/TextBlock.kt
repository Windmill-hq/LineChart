package com.contest.chart.details

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import com.contest.chart.R
import com.contest.chart.model.InterceptionInfo

class TextBlock(resources: Resources) {
    private val data = ArrayList<InterceptionInfo>()

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 20f
    }
    private val green = resources.getColor(R.color.green)
    private val red = resources.getColor(R.color.red)
    private val black = resources.getColor(R.color.black)

    private var dateLabel = "Sat, May 4"
    private var firstValue = 127
    private var firstLabel = "Joined"
    private var secondValue = 69
    private var secondLabel = "Left"

    fun draw(canvas: Canvas, left: Float, top: Float) {
        var x = left + 35
        var y = top + 35
        canvas.drawText(dateLabel, x, y, blackPaint())

        y += 50
        canvas.drawText(firstValue.toString(), x, y, greenPaint())
        y += 20
        canvas.drawText(firstLabel, x, y, greenPaint())

        x = left + 100
        y = top + 85
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

    fun setData(interceptions: List<InterceptionInfo>) {
        this.data.clear()
        this.data.addAll(interceptions)
    }
}
