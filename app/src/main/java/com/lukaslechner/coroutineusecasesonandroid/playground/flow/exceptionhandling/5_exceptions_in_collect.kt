package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val scope = CoroutineScope(Dispatchers.Default)

    stocksFlow()
        .onEach { stock ->
            throw Exception("collect{} failed")
            println("Collected: $stock")
        }.catch {
            println("Exception handled in catch ($it)")
        }
        .launchIn(scope)
        .join()
}

private fun stocksFlow(): Flow<String> = flow {
    emit("Apple")
    throw Exception("Network request failed")
}