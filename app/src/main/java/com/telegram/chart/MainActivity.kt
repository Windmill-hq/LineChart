package com.telegram.chart

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
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.subtitle = getString(R.string.author)
        dataProvider = DataProvider()

        dataProvider.getData(this, this)
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
                val isChecked = item.isChecked
                item.isChecked = !isChecked
                switchTheme(isChecked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchTheme(checked: Boolean) {
        timeLineChart.switchTheme()
        val night = resources.getColor(R.color.backGroundDark)
        val day = resources.getColor(R.color.white)
        val color = if (checked) day else night

        root.setBackgroundColor(color)
    }
}




