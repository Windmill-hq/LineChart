package com.telegram.chart

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.contest.chart.BottomControlView
import com.telegram.chart.model.DataProvider
import com.telegram.chart.model.LineChartDataMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomControlView.Listener {
    private val disposables = CompositeDisposable()
    private lateinit var dataProvider: DataProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        chart.setListener(this)
        dataProvider = DataProvider()

        dataProvider.getData(this)
            .subscribeOn(Schedulers.io())
            .map(LineChartDataMapper())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                chart.setData(it)
                Toast.makeText(this, "Data Loaded", Toast.LENGTH_LONG).show()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }).addTo(disposables)
    }

    override fun onWindowSizeChanged(left: Float, right: Float) {
        leftText.text = getString(R.string.left_edge, left.toInt())
        rightText.text = getString(R.string.right_edge, right.toInt())
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}




