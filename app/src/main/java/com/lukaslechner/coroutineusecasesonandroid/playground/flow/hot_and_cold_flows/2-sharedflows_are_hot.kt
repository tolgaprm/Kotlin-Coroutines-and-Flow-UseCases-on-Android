package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun main() {

    val sharedFlow = MutableSharedFlow<Int>()

    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        repeat(10) {
            println("SharedFlow emits $it")
            sharedFlow.emit(it)
            delay(200)
        }
    }

    Thread.sleep(500)

    val job = scope.launch {
        sharedFlow.collect {
            println("First Collector receives: $it")
        }
    }

    Thread.sleep(500)

    job.cancel()

    Thread.sleep(2000)
}