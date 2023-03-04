package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesOreo
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesPie
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VariableAmountOfNetworkRequestsViewModelTest{

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequestsSequentially() should load data sequentially`() = runTest {
        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

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

        assertEquals(4000,currentTime)
    }

    @Test
    fun `performNetworkRequestsSequentially() should load data concurrently`() = runTest {
        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

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

        assertEquals(2000,currentTime)
    }




    private fun observeViewModel(viewModel: VariableAmountOfNetworkRequestsViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}