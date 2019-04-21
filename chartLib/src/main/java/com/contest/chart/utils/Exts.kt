package com.contest.chart.utils

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.support.design.chip.Chip
import android.support.v4.content.ContextCompat
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

fun FrameLayout.createChip(
    name: String,
    color: String,
    listener: CompoundButton.OnCheckedChangeListener
): Chip {
    val chip = Chip(context).apply {
        text = name
        isClickable = true
        isCheckable = true
        minWidth = 140
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setChipStrokeWidthResource(R.dimen.stroke)
        checkedIcon = ContextCompat.getDrawable(context, R.drawable.ic_checked)
        chipStrokeColor = ColorStateList.valueOf(Color.parseColor(color))
        setTextColor(getStateColors("#FFFFFF", color))
        chipBackgroundColor = getStateColors(color, "#00FFFFFF")
        setOnCheckedChangeListener(listener)
    }
    chip.isChecked = true

    return chip
}


private fun getStateColors(checked: String, unchecked: String): ColorStateList {

    val states = arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked)
    )

    val colors = intArrayOf(Color.parseColor(checked), Color.parseColor(unchecked))

    return ColorStateList(states, colors)
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
    this.brokenLines.forEach {
        if (it.isEnabled) store.add(it.points.max() ?: 0f)
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







