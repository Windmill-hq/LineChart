package com.contest.chart.model


class BrokenLine {
    val points: ArrayList<Point> = ArrayList()
    val name: String = ""
    val color: String = ""
    val type: String = ""

    fun add(point: Point) {
        points.add(point)
    }
}

class Point(val timeX: Long, val y: Int)