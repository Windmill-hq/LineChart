package com.contest.chart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.contest.chart.model.LineChartData
import com.contest.chart.view.FocusedRangeFrame

class TimeLineChart : FrameLayout, CompoundButton.OnCheckedChangeListener {

    lateinit var chart: TimeBasedLineChart
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
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_widget, this, true)
        chart = view.findViewById<TimeBasedLineChart>(R.id.chart)
        focusedRangeFrame = view.findViewById<FocusedRangeFrame>(R.id.focus_frame)
        namesCheckBoxLayout = view.findViewById<LinearLayout>(R.id.checkbox_layout)
        focusedRangeFrame.addListener(chart)
    }

    fun addListener(listener: FocusedRangeFrame.Listener) {
        focusedRangeFrame.addListener(listener)
    }

    fun setData(dataList: List<LineChartData>) {
        chart.setData(dataList)
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
        chart.onLineStateChanged(name, isChecked)
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
        chart.switchNightMode(nightMode)
    }
}

private fun FrameLayout.createCheckBox(name: String, listener: CompoundButton.OnCheckedChangeListener): CheckBox {
    val checkBox = CheckBox(context)
    checkBox.text = name
    checkBox.isChecked = true
    checkBox.setOnCheckedChangeListener(listener)
    return checkBox
}


private fun createLayoutParams(): LinearLayout.LayoutParams {
    val params = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
    params.setMargins(12, 12, 12, 6)
    return params
}
