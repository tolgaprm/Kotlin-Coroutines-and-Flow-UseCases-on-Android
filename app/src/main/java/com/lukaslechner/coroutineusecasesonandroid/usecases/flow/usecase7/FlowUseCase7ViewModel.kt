package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowUseCase7ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    private val filterFlow = MutableSharedFlow<String?>()
    private val debouncedFilterFlow = filterFlow
        .debounce(500)
        .onStart { emit(null) }

    val currentStockPriceAsSharedFlow: Flow<UiState> =
        stockPriceDataSource
            .latestStockList
            .combine(debouncedFilterFlow) { stockList, filterTerm ->
                if (filterTerm == null) {
                    return@combine stockList
                }

                when (filterTerm.length) {
                    in (0..2) -> stockList
                    else -> stockList.filter { stock ->
                        stock.name.contains(filterTerm, ignoreCase = true)
                    }
                }
            }
            .map { stockList ->
                UiState.Success(stockList) as UiState
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = UiState.Loading,
                started = SharingStarted.WhileSubscribed(5000)
            )

    fun updateFilterTerm(filterTerm: String) {
        // why doesn't tryEmit work here?
        viewModelScope.launch {
            filterFlow.emit(filterTerm)
        }
    }
}