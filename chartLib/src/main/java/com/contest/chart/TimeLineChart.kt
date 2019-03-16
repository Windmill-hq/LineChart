package com.contest.chart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.bottom.BottomChart
import com.contest.chart.model.LineChartData
import com.contest.chart.upper.DetailsView
import com.contest.chart.upper.UpperChart
import com.contest.chart.utils.createCheckBox
import com.contest.chart.utils.createLayoutParams

class TimeLineChart : FrameLayout, CompoundButton.OnCheckedChangeListener {

    lateinit var bottomChart: BottomChart
    lateinit var upperChart: UpperChart
    lateinit var focusedRangeFrame: FocusedRangeFrame
    lateinit var namesCheckBoxLayout: LinearLayout
    lateinit var detailsView: DetailsView
    lateinit var container: LinearLayout
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
        bottomChart = view.findViewById(R.id.bottom_chart)
        upperChart = view.findViewById(R.id.upper_chart)
        focusedRangeFrame = view.findViewById(R.id.focus_frame)
        namesCheckBoxLayout = view.findViewById(R.id.checkbox_layout)
        detailsView = view.findViewById(R.id.details_view)
        container = view.findViewById(R.id.container)

        focusedRangeFrame.addListener(bottomChart)
        focusedRangeFrame.addListener(upperChart)
    }

    fun setData(dataList: List<LineChartData>) {
        bottomChart.setData(dataList)
        upperChart.setData(dataList)
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
        upperChart.onLineStateChanged(name, isChecked)
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
        val day = context.resources.getColor(R.color.backGround)
        val night = context.resources.getColor(R.color.backGroundDark)
        val color = if (nightMode) night else day
        container.setBackgroundColor(color)

        focusedRangeFrame.switchDayNightMode(nightMode)
        detailsView.switchDayNightMode(nightMode)
    }
}
