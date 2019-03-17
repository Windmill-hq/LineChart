package com.contest.chart

interface StepProvider {
    fun getStepMap(): Map<Int, Step>
}

class Step(val xStep: Float, val yStep: Float)