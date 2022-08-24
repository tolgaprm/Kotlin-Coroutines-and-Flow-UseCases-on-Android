package com.lukaslechner.coroutineusecasesonandroid.playground.flow.advancedoperators

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

suspend fun main(): Unit = coroutineScope {

    // Flattening operators are needed when one flow emission triggers a request for another
    // sequence of values

    // with a simple map, we would end up with a Flow of Flows
    (1..3).asFlow().map { requestFlow(it) }

    // try to use flattenMerge

}

fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // wait 500 ms
    emit("$i: Second")
}