package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class TeslaStockPriceLogger(
    private val stockPriceRepository: StockPriceRepository,
    private val scope: CoroutineScope
) {

    private var loggingJob: Job? = null

    fun startLogging() {
        loggingJob = stockPriceRepository
            .latestStockListFromRoomFlow
            .onEach { stockList ->
                val teslaStock = stockList.find { stock -> stock.name == "Tesla" }
                Timber.d("Tesla Price: ${teslaStock?.currentPrice}")
            }
            .launchIn(scope)
    }

    fun stopLogging() {
        loggingJob?.cancel()
    }

}

