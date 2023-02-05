package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeout: Long) {
        uiState.value = UiState.Loading
        //usingWithTimeOut(timeout)
        usingWithTimeOutOrNull(timeout)
    }

    private fun usingWithTimeOut(timeout: Long) {
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeout(timeout) {
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (e: TimeoutCancellationException) {
                uiState.value = UiState.Error("Network Request timed out!")
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Request failed!")
            }
        }
    }

    private fun usingWithTimeOutOrNull(timeout: Long) {
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeoutOrNull(timeout) {
                    api.getRecentAndroidVersions()
                }
                if (recentAndroidVersions != null){
                    uiState.value = UiState.Success(recentAndroidVersions)
                }else{
                    uiState.value = UiState.Error("Network Request timed out!")
                }
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Request failed!")
            }
        }
    }

}