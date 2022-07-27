package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase3ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsSharedFlow: Flow<UiState> =
        stockPriceDataSource
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
            .shareIn(
                scope = viewModelScope,
                replay = 0,
                started = SharingStarted.WhileSubscribed(5000)
            )
}