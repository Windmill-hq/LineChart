package com.telegram.chart

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.contest.chart.BottomControlView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomControlView.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        chart.setListener(this)
    }

    override fun onWindowSizeChanged(left: Float, right: Float) {
        leftText.text = getString(R.string.left_edge, left.toInt())
        rightText.text = getString(R.string.right_edge, right.toInt())
    }
}
