package com.contest.chart.utils

import android.graphics.Color
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout
import java.lang.StringBuilder


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
