package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

fun main() {

    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .filter {
                it > 3
            }
            .collect {
                println(it)
            }
    }
}