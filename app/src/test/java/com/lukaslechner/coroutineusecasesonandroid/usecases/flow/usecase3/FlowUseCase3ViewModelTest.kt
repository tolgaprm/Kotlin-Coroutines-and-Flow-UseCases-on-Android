package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase3

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.appleStock
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.googleStock
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FlowUseCase3ViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Test
    fun `should collect loading and success ui states on successful emissions`() = runTest {

        val fakeStockPriceDataSource = FakeStockPriceDataSource()
        val viewModel = FlowUseCase3ViewModel(fakeStockPriceDataSource)

        val collectJob =
            launch(UnconfinedTestDispatcher()) {
                viewModel.currentStockPriceAsStateFlow.collect()
            }

        assertEquals(
            UiState.Loading,
            viewModel.currentStockPriceAsStateFlow.value
        )

        fakeStockPriceDataSource.emit(
            listOf(
                googleStock,
                appleStock
            )
        )

        assertEquals(
            UiState.Success(
                listOf(
                    googleStock,
                    appleStock
                )
            ),
            viewModel.currentStockPriceAsStateFlow.value
        )

        fakeStockPriceDataSource.emit(listOf(googleStock))

        assertEquals(
            UiState.Success(
                listOf(
                    googleStock
                )
            ),
            viewModel.currentStockPriceAsStateFlow.value
        )

        collectJob.cancel()
    }
}