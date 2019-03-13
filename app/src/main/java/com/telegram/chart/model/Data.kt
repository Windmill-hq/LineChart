package com.telegram.chart.model

class Data(
    val columns: List<List<Any>>,
    val types: Map<String, String>,
    val names: Map<String, String>,
    val colors: Map<String, String>
)