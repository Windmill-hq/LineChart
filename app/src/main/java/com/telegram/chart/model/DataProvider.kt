package com.telegram.chart.model

import android.content.Context
import com.contest.chart.model.LineChartData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataProvider {

    val fileName = "chart_data.json"

    fun getData(context: Context, callBack: CallBack) {
        try {
            val content = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Data>>() {}.type
            val rawData = Gson().fromJson<List<Data>>(content, listType)
            val mapper = LineChartDataMapper()

            val chartData = mapper.apply(rawData)
            callBack.onSuccess(chartData)
        } catch (ex: java.lang.Exception) {
            callBack.onError(ex)
        }
    }

    interface CallBack {
        fun onSuccess(chartsDataList: List<LineChartData>)
        fun onError(exception: Exception)
    }
}