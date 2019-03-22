package com.telegram.chart.model

import android.content.Context
import com.contest.chart.model.LineChartData
import org.json.JSONArray
import org.json.JSONObject

class DataProvider {

    private val fileName = "chart_data.json"

    fun getDataMy(context: Context, callBack: CallBack) {
        try {
            val chartsDataList = ArrayList<Data>()
            val content = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(content)

            for (index in 0 until jsonArray.length()) {
                val rawChart = jsonArray[index] as JSONObject
                val rawColumns = rawChart.get("columns") as JSONArray
                val rawTypes = rawChart.get("types") as JSONObject
                val rawNames = rawChart.get("names") as JSONObject
                val rawColors = rawChart.get("colors") as JSONObject
                chartsDataList.add(createData(rawColumns, rawTypes, rawNames, rawColors))
            }
            val mapper = LineChartDataMapper()
            val chartData = mapper.apply(chartsDataList)

            callBack.onSuccess(chartData)
        } catch (ex: java.lang.Exception) {
            callBack.onError(ex)
        }
    }

    private fun createData(
        rawColumns: JSONArray,
        rawTypes: JSONObject,
        rawNames: JSONObject,
        rawColors: JSONObject
    ): Data {

        val names = toStringStringMap(rawNames)
        val types = toStringStringMap(rawTypes)
        val colors = toStringStringMap(rawColors)
        val columns = toListOfListOfAny(rawColumns)

        return Data(columns, types, names, colors)
    }

    private fun toListOfListOfAny(jsonArray: JSONArray): List<List<Any>> {
        val mainList = ArrayList<List<Any>>()

        for (index in 0 until jsonArray.length()) {
            val list = ArrayList<Any>()
            val childArray = jsonArray[index] as JSONArray
            for (i in 0 until childArray.length()) {
                list.add(childArray.get(i))
            }
            mainList.add(list)
        }
        return mainList
    }

    private fun toStringStringMap(jsonObject: JSONObject): Map<String, String> {
        val map = HashMap<String, String>()
        jsonObject.keys().forEach { map[it] = jsonObject.getString(it) }
        return map
    }


    interface CallBack {
        fun onSuccess(chartsDataList: List<LineChartData>)
        fun onError(exception: Exception)
    }
}