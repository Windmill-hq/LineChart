package com.contest.chart

import android.graphics.PointF
import com.contest.chart.model.InterceptionInfo
import com.contest.chart.model.LineChartData

class DataController : DataProvider {
    private lateinit var chartdata: LineChartData
    private lateinit var detailsProvider: ChartDetailsProvider

    override fun getInterceptions(x: Float): InterceptionInfo {
        if (x == 0f) return InterceptionInfo(-1)

        val positionOffset = detailsProvider.getPositionOffset()
        val height = detailsProvider.getTotalHeight()
        val step = detailsProvider.getChartStep()

        val info = InterceptionInfo(chartdata.id)
        val posInArray = getStartPositionInArray(x, step, positionOffset) + 1
        if (posInArray >= chartdata.timeLine.size || posInArray < 0) return InterceptionInfo(-1)
        info.timeLabel = chartdata.timeLine[posInArray]

        chartdata.brokenLines.forEach {
            if (it.isEnabled) {
                val data = InterceptionInfo.Data()
                data.name = it.name
                data.color = it.color
                data.point = findInterceptionPoint(it.points, x, step, positionOffset, height)
                data.yStep = step.yStep
                data.value = it.points[posInArray] // average value
                info.details.add(data)
            }
        }

        return info
    }

    private fun interpolateValue(
        points: FloatArray,
        x: Float,
        step: Step,
        positionOffset: Int,
        height: Int,
        interPoint: PointF
    ): Float {
        val startPosition = getStartPositionInArray(x, step, positionOffset)

        val startValue = points[startPosition]
        val endValue = points[startPosition + 1]

        val (startPoint, stopPoint) = defineStartAndStopPoints(startPosition, points, height, step, positionOffset)

        val startY = startPoint.y
        val stopY = stopPoint.y
        val inteceptionY = interPoint.y


        val nearStart = Math.abs(startY - inteceptionY)
        val nearStop = Math.abs(stopY - inteceptionY)


        return if (nearStart > nearStop) startValue else endValue
    }

    private fun getStartPositionInArray(x: Float, step: Step, positionOffset: Int): Int {
        val xStep = step.xStep
        return (((positionOffset * xStep) + x) / xStep).toInt()
    }

    private fun defineStartAndStopPoints(
        startPosition: Int,
        points: FloatArray,
        height: Int,
        step: Step,
        positionOffset: Int
    ): Pair<PointF, PointF> {
        //  first point of chart line is defined
        val startPointX = (startPosition - positionOffset) * step.xStep
        val startPointY = height - points[startPosition] * step.yStep

        // second point of chart line is defined
        val stopPointX = (startPosition + 1 - positionOffset) * step.xStep
        val stopPointY = height - points[startPosition + 1] * step.yStep

        val start = PointF(startPointX, startPointY)
        val stop = PointF(stopPointX, stopPointY)

        return Pair(start, stop)
    }

    /**
    Detects interception point of vertical ray on position X and real chart line

    @points  - array of Y coordinates of poits, where position in arrey plays role by defining X coord (x = pos* xStep)
    @x - x coord for tapped point on chart
    @xStep - x Step used in chart
    @yStep - y Step used in chart
    @offset - points count  skipped in the view
     */
    private fun findInterceptionPoint(
        points: FloatArray,
        x: Float,
        step: Step,
        positionOffset: Int,
        height: Int
    ): PointF {
        // x position is calculated by multiplying position (in array) on x yStep
        // x Step depends on how many point are needed to be drawn
        val approxPosInArray = getStartPositionInArray(x, step, positionOffset)

        val (startPoint, stopPoint) = defineStartAndStopPoints(approxPosInArray, points, height, step, positionOffset)

        // coords of conditional ray
        val xRay = x

        // lets imagine right triangle where chart line is hypotenuse, and define all legs
        val minX = Math.min(startPoint.x, stopPoint.x)
        val maxX = Math.max(startPoint.x, stopPoint.x)
        val horizontalLeg = maxX - minX

        val minY = Math.min(startPoint.y, stopPoint.y)
        val maxY = Math.max(startPoint.y, stopPoint.y)
        val verticalLeg = maxY - minY

        // lets count tangent from hypotenuse to horizontal leg
        val tangentToHorizontalLeg = verticalLeg / horizontalLeg

        //the ray that is parallel to Y axis (and goes on x coord) will intercept this triangle
        // lets imagine that ray cuts out a little right triangle from the big one
        // we know length of horizontal little leg, lets define  length of second vertical leg
        // it'll be needed to count  Y coord of interception point

        val horizontalLittleLeg = xRay - minX
        val verticalLittleLeg = horizontalLittleLeg * tangentToHorizontalLeg

        //depends on goes chart line up or down, add or subtract additional y value
        val interceptionY = if (startPoint.y < stopPoint.y) {
            minY + verticalLittleLeg
        } else {
            maxY - verticalLittleLeg
        }

        return PointF(xRay, interceptionY)
    }

    override fun setChartDetailsProvider(chartDetailsProvider: ChartDetailsProvider) {
        this.detailsProvider = chartDetailsProvider
    }

    fun setData(chartData: LineChartData) {
        this.chartdata = chartData
    }
}