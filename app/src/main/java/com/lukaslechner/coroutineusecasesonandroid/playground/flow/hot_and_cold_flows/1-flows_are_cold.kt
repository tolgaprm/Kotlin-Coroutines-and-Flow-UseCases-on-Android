package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

fun coldFlow() = flow {
    println("Emitting 1")
    emit(1)

    delay(1000)
    println("Emitting 2")
    emit(2)

    delay(1000)
    println("Emitting 3")
    emit(3)
}

fun main() {

    val scope = CoroutineScope(Dispatchers.Default)

    val flow = coldFlow()

    Thread.sleep(1000)

    println("Starting flow collection")

    val job = scope.launch {
        flow.onCompletion { throwable ->
            println("Flow completed with $throwable")
        }.collect { emittedItem ->
            println("Collector 1 collects: $emittedItem")
        }
    }

    Thread.sleep(1000)
    job.cancel()

    Thread.sleep(3000)
}