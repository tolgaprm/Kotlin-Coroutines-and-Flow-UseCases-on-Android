package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase5ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsSharedFlow: Flow<UiState> = stockPriceDataSource
        .latestPrice
        .map { stockList ->
            UiState.Success(stockList) as UiState
        }
        .onCompletion { throwable ->
            Timber.d("Flow has completed: $throwable")
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = UiState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

}