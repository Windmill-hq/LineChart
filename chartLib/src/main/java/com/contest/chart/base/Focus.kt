package com.contest.chart.base

interface Focus {
    fun isFocused(pos: Int): Boolean
    fun getFocusedRange(): IntRange
}