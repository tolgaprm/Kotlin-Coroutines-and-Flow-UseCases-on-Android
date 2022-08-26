package com.lukaslechner.coroutineusecasesonandroid.playground.flow.advancedoperators

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

suspend fun main() {

    val flow = flow {
        emit(1)
        delay(100)

        emit(2)
        delay(100)

        emit(3)
        delay(500)

        emit(4)
        delay(100)

        emit(5)
    }.debounce(250)
        .onStart { emit(0) }
        .collect {
            println(it)
        }


}