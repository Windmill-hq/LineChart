package com.contest.chart.base

interface DetalsProvider {
    fun isFocused(pos: Int): Boolean
    fun getFocusedRange(): IntRange
    fun getStartY():Int
}