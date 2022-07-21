package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun main() {

    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .map {
                it * 2
            }
            .collect {
                println(it)
            }
    }
}