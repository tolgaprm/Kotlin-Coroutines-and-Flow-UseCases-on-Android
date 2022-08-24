package com.lukaslechner.coroutineusecasesonandroid.playground.flow.advancedoperators

import com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils.getStartTime
import com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils.printWithTimePassed
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach

suspend fun main(): Unit = coroutineScope {

    val startTime = getStartTime(50)

    val flow1 = (1..4)
        .asFlow()
        .onEach { delay(100) }
        .onEach { printWithTimePassed("Emit $it", startTime) }

    val flow2 = listOf("A", "B", "C", "D")
        .asFlow()
        .onEach { delay(250) }
        .onEach { printWithTimePassed("Emit $it", startTime) }

    combine(flow1, flow2) { flow1Emission, flow2Emission ->
        "$flow1Emission$flow2Emission"
    }.collect { combinedEmission ->
        printWithTimePassed("Collect: $combinedEmission", startTime)
    }
}