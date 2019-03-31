package com.telegram.chart

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.contest.chart.model.LineChartData
import com.telegram.chart.model.DataProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DataProvider.CallBack {

    private lateinit var dataProvider: DataProvider

    private var isNight = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!resources.getBoolean(R.bool.isTablet)) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_main)
        dataProvider = DataProvider()
        dataProvider.getData(this, this)
        night_mode_button.setOnClickListener { switchTheme() }
    }

    override fun onSuccess(chartsDataList: List<LineChartData>) {
        timeLineChart.setData(chartsDataList)
    }

    override fun onError(exception: Exception) {
        Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
    }

    private fun switchTheme() {
        timeLineChart.switchTheme()
        isNight = !isNight
        val color = resources.getColor(R.color.white, R.color.backGroundDark, isNight)
        root.setBackgroundColor(color)
    }
}




