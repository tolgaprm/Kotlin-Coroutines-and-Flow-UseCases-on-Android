package com.lukaslechner.coroutineusecasesonandroid.playground.flow.sharedflows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun main() {

    val sharedFlow = MutableSharedFlow<Int>(replay = 1)

    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        repeat(10) {
            println("SharedFlow emits $it")
            sharedFlow.emit(it)
            delay(200)
        }
    }

    Thread.sleep(1000)

    println("Starting collection")

    scope.launch {
        sharedFlow.collect {
            println("Collector receives: $it")
        }
    }

    Thread.sleep(1000)
}