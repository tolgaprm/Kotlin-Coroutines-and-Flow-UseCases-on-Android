package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6

import com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency.scope
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.database.StockDao
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.database.mapToEntityList
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.database.mapToUiModelList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import timber.log.Timber

class StockPriceRepository(
    private val remoteDataSource: StockPriceDataSource,
    private val localDataSource: StockDao,
    private val appScope: CoroutineScope
) {

    val latestStockList: Flow<List<Stock>> = remoteDataSource
        .latestStockList
        .onStart {
            val localData = localDataSource.currentStockPrices()
            if (localData.isNotEmpty()) {
                emit(localData.mapToUiModelList())
            }
        }
        .onEach {
            localDataSource.insert(it.mapToEntityList())
        }

    private val _latestStockListFromRoomFlow = MutableSharedFlow<List<Stock>>(replay = 1)
    val latestStockListFromRoomFlow = _latestStockListFromRoomFlow.asSharedFlow()

    init {
        localDataSource
            .currentStockPricesAsFlow()
            .map { stockEntityList ->
                stockEntityList.mapToUiModelList()
            }.onEach { stockList ->
                _latestStockListFromRoomFlow.emit(stockList)
            }.launchIn(appScope)

        _latestStockListFromRoomFlow
            .subscriptionCount
            .onEach { subscriberCount ->
                Timber.d("Subscriber Count: $subscriberCount")
                if (subscriberCount > 0) {
                    startStockDataFetching()
                } else {
                    stopStockDataFetching()
                }
            }
            .launchIn(appScope)
    }

    private var stockDataFetchingJob: Job? = null

    private fun startStockDataFetching() {
        if (stockDataFetchingJob != null) {
            return
        }

        Timber.d("Start Stock Data Fetching")

        stockDataFetchingJob = remoteDataSource
            .latestStockList
            .onEach { stockList ->
                Timber.d("Fetched new stock data, inserting it into DB")
                localDataSource.insert(stockList.mapToEntityList())
            }
            .launchIn(scope)
    }

    private fun stopStockDataFetching() {
        stockDataFetchingJob?.cancel()?.also {
            Timber.d("Stop Stock Data Fetching")
        }
        stockDataFetchingJob = null
    }
}