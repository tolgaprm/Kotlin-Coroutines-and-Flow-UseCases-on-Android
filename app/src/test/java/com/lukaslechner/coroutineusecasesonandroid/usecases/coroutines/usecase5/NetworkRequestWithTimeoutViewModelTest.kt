package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NetworkRequestWithTimeoutViewModelTest{

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `should return error when timeout`()= runTest{
        val timeOut = 5000L
        val fakeApi = FakeTimeOutErrorApi(timeOut)
        val viewModel = NetworkRequestWithTimeoutViewModel(fakeApi)

        observeViewModel(viewModel)

        viewModel.performNetworkRequest(timeOut)
        advanceUntilIdle()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network Request timed out!")
            ),
            receivedUiStates
        )

    }

    private fun observeViewModel(viewModel: NetworkRequestWithTimeoutViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }

}