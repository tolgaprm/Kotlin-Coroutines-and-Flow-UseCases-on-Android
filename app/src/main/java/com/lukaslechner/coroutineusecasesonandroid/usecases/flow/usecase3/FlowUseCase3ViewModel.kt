package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase3

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import retrofit2.HttpException
import timber.log.Timber

class FlowUseCase3ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    /*
        Exercise: Flow Exception Handling

        Tasks:
        - Adjust code in StockPriceDataSource and FlowUseCase3ViewModel

        Exception Handling Goals:
        - for HttpExceptions in the datasource
            - re-collect from the flow
            - delay for 5 seconds before re-collecting the flow
        - for all other Exceptions within the whole flow pipeline
            - show toast with error message by emitting UiState.Error
     */

    val currentStockPriceAsLiveData: LiveData<UiState> = stockPriceDataSource
        .latestStockList
        .map { stockList ->
            UiState.Success(stockList) as UiState
        }
        .onStart {
            emit(UiState.Loading)
        }
        .onCompletion {
            Timber.tag("Flow").d("Flow has completed.")
        }
        .catch {throwable->
            Timber.tag("Flow").e("Enter catch operator with $throwable")
            emit(UiState.Error("Something went wrong!"))
        }
        .asLiveData()
}