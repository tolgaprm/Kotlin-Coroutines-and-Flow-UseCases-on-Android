package com.lukaslechner.coroutineusecasesonandroid.playground.flow.advancedoperators

import com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils.getStartTime
import com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils.lastPrintTime
import com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils.printWithLastPrintTime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

suspend fun main(): Unit = coroutineScope {

    lastPrintTime = getStartTime(25)

    val flow1 = (1..3)
        .asFlow()
        .onEach { delay(100) }

    val flow2 = listOf("A", "B")
        .asFlow()
        .onEach { delay(250) }

    flow1
        .flatMapLatest { flow1Emission ->
            printWithLastPrintTime("Enter flatMapLatest with $flow1Emission")
            flow2.map { flow2Emission ->
                "$flow1Emission$flow2Emission"
            }
        }.collect {
            printWithLastPrintTime(it)
        }
}