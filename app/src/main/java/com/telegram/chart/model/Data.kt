package com.telegram.chart.model

class Data(
    val columns: List<List<Any>>,
    val types: Types,
    val names: Names,
    val colors: Colors
)


class Colors(
    val y0: String,
    val y1: String,
    val y2: String,
    val y3: String
)


class Names(
    val y0: String,
    val y1: String,
    val y2: String,
    val y3: String
)

class Types(
    val x: String,
    val y0: String,
    val y1: String,
    val y2: String,
    val y3: String
)