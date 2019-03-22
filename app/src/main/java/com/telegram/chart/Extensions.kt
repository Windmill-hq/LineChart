package com.telegram.chart

import android.content.res.Resources


fun Resources.getColor(dayColorId: Int, nightColorId: Int, isNight: Boolean): Int {
    val day = getColor(dayColorId)
    val night = getColor(nightColorId)
    return if (isNight) night else day
}