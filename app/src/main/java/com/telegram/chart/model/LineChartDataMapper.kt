package com.telegram.chart.model

import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import io.reactivex.functions.Function
import java.lang.IllegalArgumentException

class LineChartDataMapper : Function<List<Data>, List<LineChartData>> {
    override fun apply(dataList: List<Data>): List<LineChartData> {
        val chartData = ArrayList<LineChartData>()

        dataList.forEachIndexed { index, data ->

            val mutableColumns = data.columns.toMutableList()

            val lineChart = LineChartData(index)

            mutableColumns.forEach { list ->
                val mutableColumn = list.toMutableList()
                val id = mutableColumn[0].toString()
                mutableColumn.removeAt(0)

                if (id == "x") {
                    lineChart.timeLine = mutableColumn.toLongArr()
                } else {
                    val points = mutableColumn.toFloatArr()
                    val colorCode = data.colors[id]
                    val line = BrokenLine(points, data.names.getValue(id), colorCode!!)
                    lineChart.brokenLines.add(line)
                }
            }

            chartData.add(lineChart)
        }

        if (chartData.isEmpty()) throw IllegalArgumentException("Data is Empty")

        return chartData
    }
}

private fun MutableList<Any>.toLongArr(): LongArray {
    val result = LongArray(size)
    var index = 0
    for (element in this) result[index++] = element.toLong()
    return result
}


private fun MutableList<Any>.toFloatArr(): FloatArray {
    val result = FloatArray(size)
    var index = 0
    for (element in this) result[index++] = element.toFloat()
    return result
}

private fun Any.toLong() = (this as Double).toLong()
private fun Any.toFloat() = (this as Double).toFloat()


