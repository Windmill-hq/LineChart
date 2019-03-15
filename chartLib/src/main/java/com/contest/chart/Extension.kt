package com.contest.chart

import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout


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
