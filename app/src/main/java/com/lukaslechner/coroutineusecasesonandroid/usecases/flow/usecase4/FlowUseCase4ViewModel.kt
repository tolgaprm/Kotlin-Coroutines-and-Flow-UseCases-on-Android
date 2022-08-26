package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase4ViewModel(
    stockPriceDataSource: StockPriceDataSource,
    defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<UiState>() {

    val currentStockPriceAsSharedFlow: Flow<UiState> = stockPriceDataSource
        .latestStockList
        .map { stockList ->
            val totalMarketCap = stockList
                .map { it.marketCap }
                .sum()
            UiState.Success(stockList, totalMarketCap) as UiState
        }.flowOn(defaultDispatcher)
        .onCompletion { throwable ->
            Timber.d("Flow has completed: $throwable")
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = UiState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

}