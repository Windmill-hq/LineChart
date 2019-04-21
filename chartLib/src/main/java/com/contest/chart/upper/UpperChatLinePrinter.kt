package com.contest.chart.upper

import android.graphics.Canvas
import android.graphics.Path
import com.contest.chart.base.BaseLinePrinter
import com.contest.chart.base.DetalsProvider
import com.contest.chart.model.BrokenLine
import com.contest.chart.utils.Constants

class UpperChatLinePrinter(
    line: BrokenLine,
    provider: DetalsProvider,
    thickness: Float
) : BaseLinePrinter(line, thickness, provider) {

    private var positionOffset = 0

    override fun draw(canvas: Canvas, xStep: Float, yStep: Float) {
        if (!line.isEnabled) return

        val range = provider.getFocusedRange()
        path.reset()

        for (positionX in range) {
            val x1 = (positionX - positionOffset) * xStep
            val x2 = (positionX + 1 - positionOffset) * xStep

            val originY1 = line.points[positionX]
            val originY2 = line.points[positionX + 1]
            val y1 = getStartY() - originY1 * yStep - Constants.BOTTOM_VERTICAL_OFFSET
            val y2 = getStartY() - originY2 * yStep - Constants.BOTTOM_VERTICAL_OFFSET

            path.moveTo(x1, y1)
            path.lineTo(x2, y2)
        }
        canvas.drawPath(path, paint)
    }

    fun offsetChanged(newOffset: Int) {
        positionOffset = newOffset
    }
}