package com.telegram.chart.model

interface Mapper<ID, OD> {
    fun apply(inputData: ID): OD
}