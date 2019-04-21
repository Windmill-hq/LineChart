package com.contest.chart.base

import android.graphics.Canvas
import com.contest.chart.model.BrokenLine

open class LinePrinter(
    line: BrokenLine,
    provider: DetalsProvider,
    thickness: Float
) : BaseLinePrinter(line, thickness, provider) {

    override fun draw(canvas: Canvas, xStep: Float, yStep: Float) {
        if (!line.isEnabled) return
        drawSimpleBottomLine(canvas, xStep, yStep)
    }
}