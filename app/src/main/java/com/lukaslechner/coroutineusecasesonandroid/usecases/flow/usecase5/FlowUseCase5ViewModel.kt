package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase5ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    private val selectedCurrencyFlow = MutableStateFlow(Currency.DOLLAR)

    fun changeCurrency() {
        // TODO:
        // This function is triggered when the user presses the currency icon in the options menu
        // Your task here is to change the value of selectedCurrencyFlow to EURO(€) if its current
        // value is DOLLAR($) and to DOLLAR($) if the current value is EURO(€)

        selectedCurrencyFlow.update { currentCurrency ->
            val newCurrency = when (currentCurrency) {
                Currency.DOLLAR -> Currency.EURO
                Currency.EURO -> Currency.DOLLAR
            }
            Timber.d("Changing currency from $currentCurrency to $newCurrency")
            newCurrency
        }
    }

    // TODO
    // Your task here is to also emit a new uiState when the user changes the currency
    // by composing this existing flow with "selectedCurrencyFlow"
    // Additionally, you should change the currency property of all emitted stocks to the selected
    // currency so that the currency changes in the UI
    val currentStockPriceAsSharedFlow: Flow<UiState> = stockPriceDataSource
        .latestPrice
        .combine(selectedCurrencyFlow) { stockList, currency ->
            Timber.d("Enter combine with currency $currency")
            delay(10_000)
            stockList.map { it.copy(currency = currency) }
        }
        .flowOn(Dispatchers.Default)
        .map { stockList ->
            UiState.Success(stockList) as UiState
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = UiState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

}