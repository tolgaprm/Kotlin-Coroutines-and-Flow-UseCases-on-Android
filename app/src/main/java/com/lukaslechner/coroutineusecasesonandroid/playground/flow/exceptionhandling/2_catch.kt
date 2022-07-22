package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        stocksFlow()
            .map {
                println("inside map")
                throw Exception("Exception thrown in map{}")
                it
            }
            .catch { throwable ->
                println("Exception handled in catch ($throwable)")
            }
            .collect { stock ->
                println("Collected: $stock")
            }
    }.join()
}

private fun stocksFlow(): Flow<String> = flow {
    emit("Apple")
    throw Exception("Network request failed")
}