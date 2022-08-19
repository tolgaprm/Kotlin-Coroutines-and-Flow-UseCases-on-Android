package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4.database.StockDao
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4.database.mapToEntityList
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4.database.mapToUiModelList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

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

}