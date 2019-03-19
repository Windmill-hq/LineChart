package com.contest.chart.utils

import android.content.res.Resources
import android.graphics.Color
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.contest.chart.model.BrokenLine
import java.lang.StringBuilder
import java.util.*


fun FrameLayout.createCheckBox(name: String, listener: CompoundButton.OnCheckedChangeListener): CheckBox {
    val checkBox = CheckBox(context)
    checkBox.text = name
    checkBox.isChecked = true
    checkBox.setOnCheckedChangeListener(listener)
    return checkBox
}


fun createLayoutParams(): LinearLayout.LayoutParams {
    val params = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
    params.setMargins(12, 12, 12, 6)
    return params
}


fun String.transparentOn(transparency: String): Int {
    val pale = StringBuilder(this).insert(1, transparency).toString()
    return Color.parseColor(pale)
}

fun BrokenLine.getSampledPoints(focusRange: IntRange): FloatArray {
    return this.points.copyOfRange(focusRange.first, focusRange.last)
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


