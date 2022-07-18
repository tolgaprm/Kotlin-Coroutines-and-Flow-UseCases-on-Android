package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.LiveData
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel

class FlowUseCase1ViewModel(
    private val StockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsLiveData: LiveData<UiState> = TODO()

}