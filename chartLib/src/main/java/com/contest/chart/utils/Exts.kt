package com.contest.chart.utils

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.contest.chart.R
import com.contest.chart.base.BaseListener
import com.contest.chart.model.BrokenLine
import com.contest.chart.model.LineChartData
import java.util.*


fun FrameLayout.createCheckBox(
    name: String,
    color: Int,
    listener: CompoundButton.OnCheckedChangeListener
): CheckBox {
    val checkBox = CheckBox(context)
    checkBox.buttonTintList = ColorStateList.valueOf(color)
    checkBox.text = name
    checkBox.setTextColor(color)
    checkBox.isChecked = true
    checkBox.setOnCheckedChangeListener(listener)
    checkBox.layoutParams = createLayoutParams()
    return checkBox
}


fun createLayoutParams(): LinearLayout.LayoutParams {
    val params = LinearLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT
    )
    params.setMargins(12, 12, 12, 6)
    return params
}


fun String.transparentOn(transparency: String): Int {
    val pale = StringBuilder(this).insert(1, transparency).toString()
    return Color.parseColor(pale)
}

fun ArrayList<BrokenLine>.getMaxValueInRange(range: IntRange, store: ArrayList<Float>): Float? {
    store.clear()
    this.forEach { line ->
        if (line.isEnabled) {
            var max = 0f
            for (index in range) {
                val value = line.points[index]
                if (value > max) max = value
            }
            store.add(max)
        }
    }

    return store.max()
}

fun Resources.getColor(dayColorId: Int, nightColorId: Int, isNight: Boolean): Int {
    val day = getColor(dayColorId)
    val night = getColor(nightColorId)
    return if (isNight) night else day
}

fun LongArray.toStringDate(): Array<String> {
    val dateStr = Array(this.size) { "" }

    this.forEachIndexed { index, date ->
        dateStr[index] = Date(date).toString().substring(4, 10)
    }
    return dateStr
}

fun LineChartData.getChartMaxValueInRange(range: IntRange, store: ArrayList<Float>): Float {
    store.clear()
    this.brokenLines.forEach { line ->
        if (line.isEnabled) {
            var max = 0f
            for (index in range) {
                val value = line.points[index]
                if (value > max) max = value
            }
            store.add(max)
        }
    }

    val max = store.max()
    return max ?: return 0f
}

fun LineChartData.getChartMaxValue(store: ArrayList<Float>): Float {
    store.clear()
    this.brokenLines.forEach { line ->
        if (line.isEnabled) {
            var max = 0f
            line.points.forEach { if (it > max) max = it }
            store.add(max)
        }
    }

    return store.max() ?: return 0f
}

fun LineChartData.getChartMaxSize(store: ArrayList<Int>, range: IntRange? = null): Int {
    store.clear()
    this.brokenLines.forEach {
        if (it.isEnabled) {
            val size = range?.count() ?: it.points.size
            store.add(size)
        }
    }

    return store.max() ?: 0
}

fun animateValue(
    startVal: Float,
    endVal: Float,
    time: Long,
    listener: BaseListener
) {

    ValueAnimator.ofFloat(startVal, endVal).apply {
        duration = time
        repeatCount = 0
        addUpdateListener(listener)
        addListener(listener)
    }.start()
}

fun FrameLayout.createSeparator(): View {
    val separator = View(context)
    separator.setBackgroundColor(resources.getColor(R.color.detailsLineColor))
    val separatorParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3)
    separatorParams.setMargins(70, 0, 0, 0)
    separator.layoutParams = separatorParams
    return separator
}







