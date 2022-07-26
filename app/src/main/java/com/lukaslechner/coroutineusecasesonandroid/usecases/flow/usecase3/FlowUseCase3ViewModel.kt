package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase3

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class FlowUseCase3ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsFlow: Flow<UiState> = stockPriceDataSource
        .latestPrice
        .map { stockList ->
            UiState.Success(stockList) as UiState
        }
        .onStart {
            emit(UiState.Loading)
        }
        .onCompletion { throwable ->
            Timber.d("Flow has completed: $throwable")
        }
}