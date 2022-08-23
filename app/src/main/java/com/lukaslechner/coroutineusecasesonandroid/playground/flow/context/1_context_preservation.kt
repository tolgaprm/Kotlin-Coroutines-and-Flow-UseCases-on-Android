package com.lukaslechner.coroutineusecasesonandroid.playground.flow.context

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import java.math.BigInteger

suspend fun main(): Unit = coroutineScope {

    launch(CoroutineName("Factorial Calculating Coroutine") + Dispatchers.IO) {
        calculateFactorialOf(10).collect { result ->
            println(result)
        }
    }
}

// factorial of n (n!) = 1 * 2 * 3 * 4 * ... * n
private fun calculateFactorialOf(number: Int) = flow {
    println("Context of flow: ${currentCoroutineContext()}")
    var factorial = BigInteger.ONE
    for (i in 1..number) {
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        delay(10)
        emit(factorial)
    }
}