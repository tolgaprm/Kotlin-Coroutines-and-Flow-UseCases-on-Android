package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class FlowUseCase1ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsLiveData: MutableLiveData<UiState> = MutableLiveData()

    init {
        stockPriceDataSource.latestStockList
            .map { stockList ->
                UiState.Success(stockList) as UiState
            }
            .onStart {
                emit(UiState.Loading)
            }
            .onEach { uiState->
                currentStockPriceAsLiveData.value = uiState
            }
            .launchIn(viewModelScope)

    }

}