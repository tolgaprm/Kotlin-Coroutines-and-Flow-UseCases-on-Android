package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.system.measureTimeMillis

suspend fun main() = coroutineScope {

    val flow = flow {
        repeat(5) {
            println("Emitter:    Start Cooking Pancake $it")
            delay(100)
            println("Emitter:    Pancake $it ready!")
            emit(it)
        }
    }
        .buffer(1, BufferOverflow.DROP_LATEST)
        .map {
            // do some mapping
            it
        }

    val duration = measureTimeMillis {
        flow.collect {
            println("Collector:  Start eating pancake $it")
            delay(300)
            println("Collector:  Finished eating pancake $it")
        }
    }

    println("==============================================")
    println("Breakfast took $duration ms")
}