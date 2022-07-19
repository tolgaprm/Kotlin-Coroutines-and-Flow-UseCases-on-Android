package com.lukaslechner.coroutineusecasesonandroid.playground.flow.builders

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val firstFlow: Flow<Int> = flowOf()

    firstFlow.collect { emittedValue ->
        println(emittedValue)
    }

    println("==========")

    val secondFlow: Flow<Int> = flowOf(1, 2, 3)

    secondFlow
        .collect { emittedValue ->
            println(emittedValue)
        }

    println("==========")

    listOf("A", "B", "C")
        .asFlow()
        .collect { emittedValue ->
            println(emittedValue)
        }

    println("==========")

    flow {
        delay(2000)
        emit("item emitted after 2000ms")

        emitAll(secondFlow)
    }.collect { emittedValue ->
        println(emittedValue)
    }
}