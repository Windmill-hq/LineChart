package com.contest.chart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import com.contest.chart.model.LineChartData
import com.contest.chart.utils.createCheckBox
import com.contest.chart.utils.createLayoutParams
import com.contest.chart.base.FocusedRangeFrame

class TimeLineChart : FrameLayout, CompoundButton.OnCheckedChangeListener {

    lateinit var bottomChart: TimeBasedLineChart
    lateinit var focusedRangeFrame: FocusedRangeFrame
    lateinit var namesCheckBoxLayout: LinearLayout
    private var nightMode = false

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
        val view = LayoutInflater.from(context).inflate(R.layout.time_line_widget, this, true)
        bottomChart = view.findViewById<TimeBasedLineChart>(R.id.bottom_chart)
        focusedRangeFrame = view.findViewById<FocusedRangeFrame>(R.id.focus_frame)
        namesCheckBoxLayout = view.findViewById<LinearLayout>(R.id.checkbox_layout)
        focusedRangeFrame.addListener(bottomChart)
    }

    fun addListener(listener: FocusedRangeFrame.Listener) {
        focusedRangeFrame.addListener(listener)
    }

    fun setData(dataList: List<LineChartData>) {
        bottomChart.setData(dataList)
        focusedRangeFrame.getFocusedRange()
        setNames(dataList)
    }

    private fun setNames(dataList: List<LineChartData>) {
        val names = getNames(dataList)
        names.forEach {
            namesCheckBoxLayout.addView(createCheckBox(it, this), createLayoutParams())
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        val name = buttonView.text.toString()
        bottomChart.onLineStateChanged(name, isChecked)
    }


    private fun getNames(dataList: List<LineChartData>): MutableList<String> {
        val names = mutableListOf<String>()
        dataList.forEach {
            it.brokenLines.forEach { line -> names.add(line.name) }
        }
        return names
    }

    fun switchDayMode() {
        nightMode = !nightMode
        bottomChart.switchDayNightMode(nightMode)
    }
}
