package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase3ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    private val _sharedFlow = MutableSharedFlow<UiState>()
    val currentStockPriceAsSharedFlow = _sharedFlow.asSharedFlow()

    init {
        stockPriceDataSource
            .latestPrice
            .onEach { stockList ->
                _sharedFlow.emit(UiState.Success(stockList))
            }
            .onStart {
                _sharedFlow.emit(UiState.Loading)
            }
            .onCompletion { throwable ->
                Timber.d("Flow has completed: $throwable")
            }
            .launchIn(viewModelScope)

    }
}