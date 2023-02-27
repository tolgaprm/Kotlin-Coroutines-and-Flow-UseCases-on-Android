package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import timber.log.Timber
import java.math.BigInteger
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition
import kotlin.system.measureTimeMillis

class CooperativeCancellationViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var calculationJob: Job? = null

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        calculationJob = viewModelScope.launch {
            var result: BigInteger = BigInteger.ONE
            val computationDuration = measureTimeMillis {
                result = calculateFactorialOf(factorialOf)
            }

            var resultString = ""

            val stringConversionDuration = measureTimeMillis {
                resultString = withContext(Dispatchers.Default) { result.toString() }
            }

            uiState.value =
                UiState.Success(resultString, computationDuration, stringConversionDuration)
        }

        calculationJob?.invokeOnCompletion { throwable ->
            if (throwable is CancellationException) {
                Timber.d("Calculation was cancelled!")
            }
        }
    }

    private suspend fun calculateFactorialOf(number: Int) = withContext(Dispatchers.Default) {
        var factorial = BigInteger.ONE
        for (i in 1..number) {
            yield() // Bu ifade içerisinde çalıştığı
            ensureActive()
            factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        }
        Timber.d("Calculating Factorial Completed!")
        factorial
    }

    fun cancelCalculation() {
        calculationJob?.cancel()
    }

    fun uiState(): LiveData<UiState> = uiState

    private val uiState: MutableLiveData<UiState> = MutableLiveData()
}