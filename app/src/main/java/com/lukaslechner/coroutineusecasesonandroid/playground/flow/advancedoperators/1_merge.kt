package com.lukaslechner.coroutineusecasesonandroid.playground.flow.advancedoperators

import com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils.getStartTime
import com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils.printWithTimePassed
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

suspend fun main(): Unit = coroutineScope {

    val startTime = getStartTime(50)

    val flow1 = (1 .. 5)
        .asFlow()
        .onEach { delay(100) }

    val flow2 = listOf("A", "B", "C", "D", "E")
        .asFlow()
        .onEach { delay(250) }

    merge(flow1, flow2)
        .collect { mergedEmission: Any ->
            printWithTimePassed(mergedEmission, startTime)
        }
}