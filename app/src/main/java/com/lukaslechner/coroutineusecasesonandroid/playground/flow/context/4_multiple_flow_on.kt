package com.lukaslechner.coroutineusecasesonandroid.playground.flow.context

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

suspend fun main(): Unit = coroutineScope {

    launch(CoroutineName("CollectContext")) {
        flow {
            println("context of flow{}: ${currentCoroutineContext()}")
            emit(1)
        }
            .map {
                println("Context of map{}: ${currentCoroutineContext()}")
                it * it
            }
            .flowOn(CoroutineName("MapContext") + Dispatchers.IO)
            .filter {
                println("Context of filter{}: ${currentCoroutineContext()}")
                it < 2
            }
            .flowOn(CoroutineName("FilterContext"))
            .collect {
                println("Context of collect{}: ${currentCoroutineContext()}")
                println(it)
            }
    }
}