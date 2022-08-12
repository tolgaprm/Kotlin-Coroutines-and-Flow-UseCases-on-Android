package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4.database.StockDao

class StockPriceRepository(
    val remoteDataSource: StockPriceDataSource,
    val localDataSource: StockDao
) {

}