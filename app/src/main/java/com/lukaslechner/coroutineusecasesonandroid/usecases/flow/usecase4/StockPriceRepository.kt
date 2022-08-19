package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4.database.StockDao
import kotlinx.coroutines.CoroutineScope

class StockPriceRepository(
    private val remoteDataSource: StockPriceDataSource,
    private val localDataSource: StockDao,
    private val appScope: CoroutineScope
) {

}