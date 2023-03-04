package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1.PerformSingleNetworkRequestViewModel
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class Perform2SequentialNetworkRequestsViewModelTest {

    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `should return success when both network request are successful`() {
        val fakeSuccessApi = FakeSuccessApi()

        val viewModel = Perform2SequentialNetworkRequestsViewModel(fakeSuccessApi)

        observeViewModel(viewModel)

        viewModel.perform2SequentialNetworkRequest()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockVersionFeaturesAndroid10)
            ),
            receivedUiStates
        )
    }


    @Test
    fun `should return error when first network request are fails`(){
        val fakeVersionsErrorApi = FakeVersionsErrorApi()

        val viewModel = Perform2SequentialNetworkRequestsViewModel(fakeVersionsErrorApi)

        observeViewModel(viewModel)

        viewModel.perform2SequentialNetworkRequest()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network request failed!")
            ),
            receivedUiStates
        )
    }

    @Test
    fun `should return error when second network request are fails`(){
        val fakeVersionsErrorApi = FakeFeaturesErrorApi()

        val viewModel = Perform2SequentialNetworkRequestsViewModel(fakeVersionsErrorApi)

        observeViewModel(viewModel)

        viewModel.perform2SequentialNetworkRequest()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network request failed!")
            ),
            receivedUiStates
        )
    }

    private fun observeViewModel(viewModel: Perform2SequentialNetworkRequestsViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}