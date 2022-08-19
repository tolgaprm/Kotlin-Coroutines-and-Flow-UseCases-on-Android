package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import kotlinx.coroutines.CoroutineScope

class TeslaStockPriceLogger(
    private val stockPriceRepository: StockPriceRepository,
    private val scope: CoroutineScope
) {

}

