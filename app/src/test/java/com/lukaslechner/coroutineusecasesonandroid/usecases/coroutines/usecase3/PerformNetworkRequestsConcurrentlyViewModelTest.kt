package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.*
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PerformNetworkRequestsConcurrentlyViewModelTest {

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()


    @Test
    fun `performNetworkRequestsSequentially() should load data sequentially`() =  runTest {

        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)

        val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)

        observeViewModel(viewModel)

        viewModel.performNetworkRequestsSequentially()
        advanceUntilIdle()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(
                    listOf(
                        mockVersionFeaturesOreo,
                        mockVersionFeaturesPie,
                        mockVersionFeaturesAndroid10
                    )
                )
            ),
            receivedUiStates
        )

        assertEquals(3000, currentTime)
    }

    @Test
    fun `performNetworkRequestsConcurrently() should load data concurrently`() = runTest {
        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)

        val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)

        observeViewModel(viewModel)

        viewModel.performNetworkRequestsConcurrently()
        advanceUntilIdle()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(
                    listOf(
                        mockVersionFeaturesOreo,
                        mockVersionFeaturesPie,
                        mockVersionFeaturesAndroid10
                    )
                )
            ),
            receivedUiStates
        )
        assertEquals(1000, currentTime)
    }


    private fun observeViewModel(viewModel: PerformNetworkRequestsConcurrentlyViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}