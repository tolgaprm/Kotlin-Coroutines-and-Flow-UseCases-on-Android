package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FlowUseCase5ViewModel(
    stockPriceRepository: StockPriceRepository
) : BaseViewModel<UiState>() {

    val latestStockList: Flow<UiState> = stockPriceRepository
        .latestStockListFromRoomFlow
        .map { stockList ->
            UiState.Success(stockList) as UiState
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = UiState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )
}