package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {

            val numberOfRetries = 2
            try {
                repeat(numberOfRetries) {
                    try {
                        loadRecentAndroidVersions()
                        return@launch
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
                loadRecentAndroidVersions()
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Request failed!")
            }
        }
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }

}