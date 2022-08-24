package com.lukaslechner.coroutineusecasesonandroid.playground.flow.utils

fun printWithTimePassed(message: Any?, startTime: Long) {
    val timePassed = System.currentTimeMillis() - startTime
    print("$timePassed: ")
    println(message)
}

fun getStartTime(timeUntilFirstFlowEmission: Long) = System.currentTimeMillis() + timeUntilFirstFlowEmission
