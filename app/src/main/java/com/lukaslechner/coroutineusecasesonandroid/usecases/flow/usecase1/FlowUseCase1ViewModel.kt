package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class FlowUseCase1ViewModel(
    private val stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    init {
        viewModelScope.launch {
            stockPriceDataSource.latestPrice.collect {
                Timber.d("Received item: ${it.first()}")
            }
        }
    }

    val currentStockPriceAsLiveData: LiveData<UiState> = MutableLiveData()

}