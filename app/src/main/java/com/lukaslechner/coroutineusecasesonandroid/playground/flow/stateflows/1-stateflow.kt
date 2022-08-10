package com.lukaslechner.coroutineusecasesonandroid.playground.flow.stateflows

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun main() {

    val stateFlow = MutableStateFlow<Int>(0)
    println(stateFlow.value)

    CoroutineScope(Dispatchers.Default).launch {
        stateFlow.collect{
            println("Collected: $it")
        }
    }

    Thread.sleep(100)

    stateFlow.value = 3
    println(stateFlow.value)

    stateFlow.update { currentValue ->
        currentValue + 1
    }
    println(stateFlow.value)

    stateFlow.value = 4

    Thread.sleep(100)
}