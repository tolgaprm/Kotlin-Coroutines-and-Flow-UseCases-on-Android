package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.datasources.ExchangeRateDataSource
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.datasources.StockPriceDataSource

class ViewModelFactory(private val stockPriceDataSource: StockPriceDataSource, private val exchangeRateDataSource: ExchangeRateDataSource) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(StockPriceDataSource::class.java, ExchangeRateDataSource::class.java)
            .newInstance(stockPriceDataSource, exchangeRateDataSource)
    }
}