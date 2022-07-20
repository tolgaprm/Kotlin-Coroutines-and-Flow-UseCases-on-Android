package com.lukaslechner.coroutineusecasesonandroid.playground.flow.terminal_operators

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

fun main() {

    val flow = flow {
        delay(100)

        println("Emitting first value")
        emit(1)

        delay(100)

        println("Emitting second value")
        emit(2)
    }

    val scope = CoroutineScope(EmptyCoroutineContext)

    val job = flow
        .onEach { println("Received: $it") }
        .launchIn(scope)

    runBlocking {
        job.join()
    }
}