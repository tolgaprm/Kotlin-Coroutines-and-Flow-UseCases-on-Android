package com.lukaslechner.coroutineusecasesonandroid.playground.flow.channels

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {

    val channel = produce {
        delay(100) // some computation
        send(0)
        send(1)
        send(2)
    }

    launch {
        channel.consumeEach { receivedValue ->
            println("Receiver1: $receivedValue")
        }
    }

    launch {
        channel.consumeEach { receivedValue ->
            println("Receiver2: $receivedValue")
        }
    }
}