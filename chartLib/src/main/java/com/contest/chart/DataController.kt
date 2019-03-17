package com.contest.chart

import android.graphics.PointF
import com.contest.chart.model.InterceptionInfo
import com.contest.chart.model.LineChartData

class DataController : DataProvider {
    private lateinit var chartList: List<LineChartData>
    private lateinit var detailsProvider: ChartDetailsProvider

    override fun getInterceptions(x: Float, offset: Int): List<InterceptionInfo> {
        if (x == 0f) return emptyList()

        val interceptionsList = ArrayList<InterceptionInfo>()

        val xScalesMap = detailsProvider.getStepMap()

        val firstChartSteps = xScalesMap.getValue(0)
        val firstChart = chartList.get(0)

        val info = InterceptionInfo(firstChart.id)

        firstChart.brokenLines.forEach {
            if (it.isEnabled) {
                val data = InterceptionInfo.Data()
                data.name = it.name
                data.color = it.color
                data.point = findInterceptionPoint(it.points, x, firstChartSteps.xStep, firstChartSteps.yStep, offset)
                data.yStep = firstChartSteps.yStep
                info.details.add(data)
            }
        }

        interceptionsList.add(info)

        return interceptionsList
    }

    /**
    Detects interception point of vertical ray on position X and real chart line

    @points  - array of Y coordinates of poits, where position in arrey plays role by defining X coord (x = pos* xStep)
    @x - x coord for tapped point on chart
    @xStep - x Step used in chart
    @yStep - y Step used in chart
    @offset - points count  skipped in the view
     */
    private fun findInterceptionPoint(points: FloatArray, x: Float, xStep: Float, yStep: Float, offset: Int): PointF {
        // x position is calculated by multiplying position (in array) on x yStep
        // x Step depends on how many point are needed to be drawn
        val approxPosInArray = (((offset * xStep) + x) / xStep).toInt()

        //  first point of chart line is defined
        val startPointX = (approxPosInArray - offset) * xStep
        val startPointY = detailsProvider.getTotalHeight() - points[approxPosInArray] * yStep

        // second point of chart line is defined
        val stopPointX = (approxPosInArray + 1 - offset) * xStep
        val stopPointY = detailsProvider.getTotalHeight() - points[approxPosInArray + 1] * yStep

        // coords of conditional ray
        val xRay = x

        // lets imagine right triangle where chart line is hypotenuse
        val minX = Math.min(startPointX, stopPointX)
        val maxX = Math.max(startPointX, stopPointX)
        val horizontalLeg = maxX - minX

        val minY = Math.min(startPointY, stopPointY)
        val maxY = Math.max(startPointY, stopPointY)
        val verticalLeg = maxY - minY

        // lets count tangent from hypotenuse to horizontal leg
        val tangentToHorizontalLeg = verticalLeg / horizontalLeg

        //the ray that is parallel to Y axis (and goes on x coord) will intercept this triangle
        // lets imagine that ray cuts out a little right triangle from the big one
        // we know length of horizontal little leg  and the second leg will be needed Y coord of interception point

        val horizontalLittleLeg = xRay - minX
        val verticalLittleLeg = horizontalLittleLeg * tangentToHorizontalLeg

        val interceptionY = if (startPointY < stopPointY) {
            verticalLittleLeg + minY
        } else {
            maxY - verticalLittleLeg
        }

        return PointF(xRay, interceptionY)
    }

    override fun setChartDetailsProvider(chartDetailsProvider: ChartDetailsProvider) {
        this.detailsProvider = chartDetailsProvider
    }

    fun setData(dataList: List<LineChartData>) {
        this.chartList = dataList
    }
}