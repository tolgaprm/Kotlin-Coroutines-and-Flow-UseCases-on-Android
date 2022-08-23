package com.lukaslechner.coroutineusecasesonandroid.playground.flow.context

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

suspend fun main(): Unit = coroutineScope {

    flow {
        repeat(5) {
            delay(50)
            println("Emit: $it")
            emit(it)
        }
    }
        .map {
            it * it
        }
        .buffer(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .flowOn(CoroutineName("SomeContext") + Dispatchers.IO)
        .collect {
            delay(200)
            println("Collect: $it")
        }

}