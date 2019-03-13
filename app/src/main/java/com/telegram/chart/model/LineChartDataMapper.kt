package com.telegram.chart.model

import android.util.Log
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import com.contest.chart.model.Point
import io.reactivex.functions.Function

class LineChartDataMapper : Function<List<Data>, List<LineChartData>> {
    override fun apply(dataList: List<Data>): List<LineChartData> {
        val chartData = ArrayList<LineChartData>()

        dataList.forEach { data ->
            var indexToMoveToZero = 0
            val mutableList = data.columns.toMutableList()

            mutableList.forEachIndexed { index, list ->
                if (list[0] == "x") indexToMoveToZero = index
            }

            val moveToZero = mutableList.removeAt(indexToMoveToZero)
            mutableList.add(0, moveToZero)

            val line1 = BrokenLine()
            val line2 = BrokenLine()
            val size = mutableList[0].size
            for (i in 1 until size) {
                val timeX = mutableList[0][i].toLong()
                val y1 = mutableList[1][i].toInt()
                val y2 = mutableList[2][i].toInt()
                val point1 = Point(timeX, y1)
                val point2 = Point(timeX, y2)

                line1.add(point1)
                line2.add(point2)
            }
            val lineData = LineChartData(arrayListOf(line1, line2))
            chartData.add(lineData)
        }

        return chartData
    }
}

private fun Any.toLong() = (this as Double).toLong()

private fun Any.toInt() = (this as Double).toInt()
