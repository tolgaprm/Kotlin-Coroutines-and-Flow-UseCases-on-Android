package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase2

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class FlowUseCase2ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    /*

    Flow exercise 1 Goals
        - only update stock list when Alphabet(Google) stock is > 2350$
        - only show stocks of America
        - filter out apple and microsoft, so that google is number one
        - only show company if it is one of the biggest 15 companies worldwide
        - stop flow collection after 10 emissions from the dataSource
        - log out the number of the current emission so that we can check if flow collection stops after exactly 5 emissions
        - start flow collection on a background thread
        - emit Error UiState on exceptions (the activity will show a toast in this case)

     */

    val currentStockPriceAsLiveData: LiveData<UiState> = stockPriceDataSource
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
        .asLiveData()
}