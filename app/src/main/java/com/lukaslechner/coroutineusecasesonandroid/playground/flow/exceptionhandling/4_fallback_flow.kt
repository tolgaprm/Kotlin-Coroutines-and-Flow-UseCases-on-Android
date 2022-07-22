package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        stocksFlow()
            .catch {
                println("Exception handled in catch ($it)")
                emitAll(fallbackFlow())
            }.catch {
                println("Exception handled in second catch ($it)")
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

private fun fallbackFlow(): Flow<String> = flow {
    emit("Microsoft")
    emit("Google")
    throw Exception("FallbackFlow failed")
}