package com.contest.chart.model

import android.graphics.PointF

class InterceptionInfo(val id: Int) {

    val details = ArrayList<Data>()
    var timeLabel = 1L

    class Data {
        var name: String = ""
        var color: String = "#010101"
        var point: PointF = PointF()
        var yStep: Float = 0f
        var value = 0f
    }
}