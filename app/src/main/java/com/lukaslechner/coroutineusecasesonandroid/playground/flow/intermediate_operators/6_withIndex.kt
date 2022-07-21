package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.runBlocking

fun main() {

    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .withIndex()
            .collect {
                println(it)
            }
    }
}