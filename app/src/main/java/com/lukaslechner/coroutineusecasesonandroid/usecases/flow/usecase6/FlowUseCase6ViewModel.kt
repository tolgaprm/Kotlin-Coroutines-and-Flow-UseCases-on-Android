package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Currency
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.datasources.ExchangeRateDataSource
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.datasources.StockPriceDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase6ViewModel(
    stockPriceDataSource: StockPriceDataSource,
    exchangeRateDataSource: ExchangeRateDataSource
) : BaseViewModel<UiState>() {

    private val selectedCurrencyFlow = MutableStateFlow(Currency.DOLLAR)

    private val exchangeRateFlow = exchangeRateDataSource
        .latestExchangeRate
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

    fun changeCurrency() {
        selectedCurrencyFlow.update { currentCurrency ->
            val newCurrency = when (currentCurrency) {
                Currency.DOLLAR -> Currency.EURO
                Currency.EURO -> Currency.DOLLAR
            }
            Timber.d("Changing currency from $currentCurrency to $newCurrency")
            newCurrency
        }
    }

    // TODO:
    // Whenever a new stock list is emitted and the currency of the stocks is dollar, then
    // collect the latestExchangeRate flow of the ExchangeRateDataSource and calculate the
    // Euro price by multiplying the dollar price with the usdInEuro property of the exchangeRate
    // object that ExchangeRateDataSource emits
    //
    // if the currency of the stocks is Dollar, no calculation should happen and the ExchangeRateDataSource
    // also shouldn't emit values (hint: use the latestExchangeRate flow as a sharedFlow)
    val currentStockPriceAsSharedFlow: Flow<UiState> = stockPriceDataSource
        .latestPrice
        .combine(selectedCurrencyFlow) { stockList, currency ->
            Timber.d("Enter combine with currency $currency")
            stockList.map { it.copy(currency = currency) }
        }.flatMapLatest { stockList ->
            if (stockList.first().currency == Currency.EURO) {
                exchangeRateFlow
                    .map { exchangeRate ->
                        stockList.map { stock ->
                            stock.copy(currentPrice = stock.currentPrice * exchangeRate.usdInEuro) }
                    }
            } else {
                flowOf(stockList)
            }
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