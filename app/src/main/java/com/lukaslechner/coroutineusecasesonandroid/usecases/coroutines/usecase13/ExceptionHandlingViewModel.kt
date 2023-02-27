package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                api.getAndroidVersionFeatures(apiLevel = 27)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network Request failed $e")
            }
        }
    }

    fun handleWithCoroutineExceptionHandler() {
        uiState.value = UiState.Loading

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            uiState.value = UiState.Error("Network Request failed $throwable")

        }

        viewModelScope.launch(exceptionHandler) {
            api.getAndroidVersionFeatures(apiLevel = 27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            supervisorScope {
                val oreoFeatureDeferred = async {
                    api.getAndroidVersionFeatures(apiLevel = 27)
                }
                val pieFeatureDeferred = async {
                    api.getAndroidVersionFeatures(apiLevel = 28)
                }
                val android10FeatureDeferred = async {
                    api.getAndroidVersionFeatures(apiLevel = 29)
                }

                val versionFeatures = listOf(
                    oreoFeatureDeferred,
                    pieFeatureDeferred,
                    android10FeatureDeferred
                ).mapNotNull {
                    try {
                        it.await()
                    } catch (e: Exception) {
                        if (e is CancellationException) {
                            throw e
                        }
                        Timber.e("Error loading feature data!")
                        null
                    }
                }

                uiState.value = UiState.Success(versionFeatures)
            }
        }
    }
}