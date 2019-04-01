package com.contest.chart

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.contest.chart.base.FocusedRangeFrame
import com.contest.chart.bottom.BottomChart
import com.contest.chart.details.DetailsView
import com.contest.chart.model.LineChartData
import com.contest.chart.upper.UpperChart
import com.contest.chart.utils.createCheckBox
import com.contest.chart.utils.createLayoutParams
import com.contest.chart.utils.getColor

class TimeLineChart : FrameLayout, CompoundButton.OnCheckedChangeListener {

    var bottomChart: BottomChart
    var upperChart: UpperChart
    var focusedRangeFrame: FocusedRangeFrame
    var namesLayout: LinearLayout
    var detailsView: DetailsView
    var container: LinearLayout
    var title: TextView
    private var nightMode = false
    private val dataController = DataController()

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
        title = view.findViewById(R.id.title)

        detailsView = view.findViewById(R.id.details_view)
        detailsView.setDataProvider(dataController)
        dataController.setChartDetailsProvider(upperChart)
        focusedRangeFrame.addListener(upperChart)
    }

    fun setTitle(name: String) {
        title.text = name
    }

    fun setData(chartData: LineChartData) {
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

    fun setParent(scrollView: LockableScrollView) {
        detailsView.setParent(scrollView)
    }
}