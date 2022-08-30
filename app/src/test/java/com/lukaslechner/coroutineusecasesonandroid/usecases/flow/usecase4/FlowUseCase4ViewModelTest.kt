package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.appleStock
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.googleStock
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FlowUseCase4ViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentStockPriceAsSharedFlow() = runTest {

        val fakeDataSource = FakeStockPriceDataSource()
        val viewModel = FlowUseCase4ViewModel(
            stockPriceDataSource = fakeDataSource,

            // this doesn't work with StandardTestDispatcher, why?
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.currentStockPriceAsStateFlow.collect()
        }

        assertEquals(
            UiState.Loading,
            viewModel.currentStockPriceAsStateFlow.value
        )

        fakeDataSource.emit(listOf(googleStock, appleStock))

        assertEquals(
            UiState.Success(listOf(googleStock, appleStock), totalMarketCap = 3.0f),
            viewModel.currentStockPriceAsStateFlow.value
        )

        collectJob.cancel()
    }
}