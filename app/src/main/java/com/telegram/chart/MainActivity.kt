package com.telegram.chart

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.contest.chart.model.LineChartData
import com.telegram.chart.model.DataProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DataProvider.CallBack {

    private lateinit var dataProvider: DataProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!resources.getBoolean(R.bool.isTablet))
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.subtitle = getString(R.string.author)
        dataProvider = DataProvider()

        dataProvider.getDataMy(this, this)
    }

    override fun onSuccess(chartsDataList: List<LineChartData>) {
        timeLineChart.setData(chartsDataList)
    }

    override fun onError(exception: Exception) {
        Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.night_mode -> {
                item.isChecked = !item.isChecked
                switchTheme(item.isChecked)
                timeLineChart.switchTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchTheme(isNight: Boolean) {
        val color = resources.getColor(R.color.white, R.color.backGroundDark, isNight)
        root.setBackgroundColor(color)
    }
}




