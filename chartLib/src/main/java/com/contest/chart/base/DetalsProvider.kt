package com.contest.chart.base

interface DetalsProvider {
    fun getFocusedRange(): IntRange
    fun getStartY():Int
}