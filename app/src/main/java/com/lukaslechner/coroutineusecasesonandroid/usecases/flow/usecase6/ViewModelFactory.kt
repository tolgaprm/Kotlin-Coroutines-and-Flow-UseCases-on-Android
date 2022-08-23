package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val stockPriceRepository: StockPriceRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(StockPriceRepository::class.java)
            .newInstance(stockPriceRepository)
    }
}