package com.telegram.chart

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.telegram.chart.model.DataProvider
import com.telegram.chart.model.LineChartDataMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private lateinit var dataProvider: DataProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        dataProvider = DataProvider()

        dataProvider.getData(this)
                .subscribeOn(Schedulers.io())
                .map(LineChartDataMapper())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    timeLineChart.setData(it)
                }, {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }).addTo(disposables)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.night_mode -> {
                timeLineChart.switchDayMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}




