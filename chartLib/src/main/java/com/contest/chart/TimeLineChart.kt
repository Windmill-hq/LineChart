package com.contest.chart

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.bottom.BottomChart
import com.contest.chart.model.LineChartData
import com.contest.chart.details.DetailsView
import com.contest.chart.upper.UpperChart
import com.contest.chart.utils.createCheckBox
import com.contest.chart.utils.createLayoutParams
import com.contest.chart.utils.getColor

class TimeLineChart : FrameLayout, CompoundButton.OnCheckedChangeListener {

    private val chartDataList = ArrayList<LineChartData>()
    lateinit var bottomChart: BottomChart
    lateinit var upperChart: UpperChart
    lateinit var focusedRangeFrame: FocusedRangeFrame
    lateinit var namesLayout: LinearLayout
    lateinit var detailsView: DetailsView
    lateinit var container: LinearLayout
    private var nightMode = false
    private val dataController = DataController()
    private var checkedChart = 0

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
        container = view.findViewById(R.id.container)
        upperChart = view.findViewById(R.id.upper_chart)
        bottomChart = view.findViewById(R.id.bottom_chart)
        focusedRangeFrame = view.findViewById(R.id.focus_frame)
        namesLayout = view.findViewById(R.id.checkbox_layout)

        detailsView = view.findViewById(R.id.details_view)
        detailsView.setDataProvider(dataController)
        dataController.setChartDetailsProvider(upperChart)

        focusedRangeFrame.addListener(bottomChart)
        focusedRangeFrame.addListener(upperChart)
        focusedRangeFrame.addListener(detailsView)

        view.findViewById<TextView>(R.id.title).setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val values = Array(chartDataList.size) { "" }

        chartDataList.forEachIndexed { index, _ -> values[index] = "Chart #$index" }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose chart!")
        builder.setSingleChoiceItems(values, checkedChart) { dialog, which ->
            checkedChart = which
            setChartData(chartDataList[which])
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()

    }

    fun setData(dataList: List<LineChartData>) {
        chartDataList.clear()
        chartDataList.addAll(dataList)

        setChartData(dataList.first())
    }

    private fun setChartData(chartData: LineChartData) {
        chartData.brokenLines.forEach { it.isEnabled = true }
        dataController.setData(chartData)
        bottomChart.setData(chartData)
        upperChart.setData(chartData)
        setNames(chartData)
        focusedRangeFrame.getFocusedRange()
    }

    private fun setNames(chartData: LineChartData) {
        val names = getNames(chartData)
        namesLayout.removeAllViews()
        names.forEach {
            namesLayout.addView(createCheckBox(it, this), createLayoutParams())
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        val name = buttonView.text.toString()
        bottomChart.onLineStateChanged(name, isChecked)
        upperChart.onLineStateChanged(name, isChecked)
        detailsView.refresh()
    }

    private fun getNames(chartData: LineChartData): MutableList<String> {
        val names = mutableListOf<String>()
        chartData.brokenLines.forEach { line -> names.add(line.name) }
        return names
    }

    fun switchTheme() {
        nightMode = !nightMode
        val color = resources.getColor(R.color.backGround, R.color.backGroundDark, nightMode)
        container.setBackgroundColor(color)

        focusedRangeFrame.switchDayNightMode(nightMode)
        detailsView.switchDayNightMode(nightMode)
    }
}
