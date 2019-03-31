package com.contest.chart

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.bottom.BottomChart
import com.contest.chart.details.DetailsView
import com.contest.chart.model.LineChartData
import com.contest.chart.upper.UpperChart
import com.contest.chart.utils.createCheckBox
import com.contest.chart.utils.createLayoutParams
import com.contest.chart.utils.getColor


class TimeLineChart : FrameLayout, CompoundButton.OnCheckedChangeListener {

    private val chartDataList = ArrayList<LineChartData>()
    var bottomChart: BottomChart
    var upperChart: UpperChart
    var focusedRangeFrame: FocusedRangeFrame
    var namesLayout: LinearLayout
    var detailsView: DetailsView
    var container: LinearLayout
    private var nightMode = false
    private val dataController = DataController()
    private var checkedChart = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.time_line_widget, this, true)
        container = view.findViewById(R.id.container)
        upperChart = view.findViewById(R.id.upper_chart)
        bottomChart = view.findViewById(R.id.bottom_chart)
        focusedRangeFrame = view.findViewById(R.id.focus_frame)
        namesLayout = view.findViewById(R.id.checkbox_layout)

        detailsView = view.findViewById(R.id.details_view)
        detailsView.setDataProvider(dataController)
        dataController.setChartDetailsProvider(upperChart)

        focusedRangeFrame.addListener(upperChart)

        spinner = view.findViewById(R.id.chart_spinner)
    }

    var spinner: Spinner

    private fun prepareSpinner() {
        val values = ArrayList<String>()
        chartDataList.forEachIndexed { index, _ -> values.add("Chart $index") }

        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checkedChart = position
                setChartData(chartDataList[position])
                focusedRangeFrame.getFocusedRange()
            }
        }
    }

    fun setData(dataList: List<LineChartData>) {
        chartDataList.clear()
        chartDataList.addAll(dataList)
        prepareSpinner()
//        setChartData(dataList.last())
    }

    private fun setChartData(chartData: LineChartData) {
        chartData.brokenLines.forEach { it.isEnabled = true }
        dataController.setData(chartData)
        bottomChart.setData(chartData)
        upperChart.setData(chartData)
        setNames(chartData)
    }

    private fun setNames(chartData: LineChartData) {
        namesLayout.removeAllViews()
        chartData.brokenLines.forEach {
            namesLayout.addView(createCheckBox(it.name, Color.parseColor(it.color), this), createLayoutParams())
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        val name = buttonView.text.toString()
        bottomChart.onLineStateChanged(name, isChecked)
        upperChart.onLineStateChanged(name, isChecked)
        detailsView.refresh()
    }

    fun switchTheme() {
        nightMode = !nightMode
        val color = resources.getColor(R.color.backGround, R.color.backGroundDark, nightMode)
        container.setBackgroundColor(color)

        focusedRangeFrame.switchDayNightMode(nightMode)
        detailsView.switchDayNightMode(nightMode)
    }
}