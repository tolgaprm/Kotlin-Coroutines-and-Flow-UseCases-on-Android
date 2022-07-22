package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun main() = runBlocking {

    val ceh = CoroutineExceptionHandler{ context, throwable ->
        println("Handled exception in CEH")
    }

    val scope = CoroutineScope(Dispatchers.Default + ceh)

    scope.launch {
        val mappedFlow = stocksFlow().map {
            throw Exception("Exception during mapping")
        }
        try {
            mappedFlow.collect { stock ->
                println("Collected: $stock")
            }
        } catch (exception: Exception) {
            println("Handle exception in try-catch block: $exception")
        }
    }.join()


}

private fun stocksFlow(): Flow<String> = flow {
    emit("Apple")
    throw Exception("Network request failed")
}