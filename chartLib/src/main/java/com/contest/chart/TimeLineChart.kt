package com.contest.chart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.contest.chart.model.LineChartData
import com.contest.chart.view.FocusedRangeFrame

class TimeLineChart : FrameLayout {
    lateinit var chart: TimeBasedLineChart
    lateinit var focusedRangeFrame: FocusedRangeFrame

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_widget, this, true)
        chart = view.findViewById<TimeBasedLineChart>(R.id.chart)
        focusedRangeFrame = view.findViewById<FocusedRangeFrame>(R.id.focus_frame)
        focusedRangeFrame.addListener(chart)
    }

    fun addListener(listener: FocusedRangeFrame.Listener) {
        focusedRangeFrame.addListener(listener)
    }

    fun setData(dataLis: List<LineChartData>) {
        chart.setData(dataLis)
        focusedRangeFrame.getFocusedRange()
    }
}
