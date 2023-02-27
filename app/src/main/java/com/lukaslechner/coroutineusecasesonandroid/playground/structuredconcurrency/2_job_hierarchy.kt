package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.*

fun main() {

    val scopeJob = Job()
    val scope = CoroutineScope(Dispatchers.Default + scopeJob)


    val coroutineJob = scope.launch {

        println("Starting Coroutine")
        delay(1000)
    }

    println("Is coroutineJob a child of scopeJob? => ${scopeJob.children.contains(coroutineJob)}")
}