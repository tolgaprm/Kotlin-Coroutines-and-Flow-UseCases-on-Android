package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.datasources

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.ExchangeRate
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.FlowMockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

interface ExchangeRateDataSource {
    val latestExchangeRate: Flow<ExchangeRate>
}

class NetworkExchangeRateDataSource(mockApi: FlowMockApi): ExchangeRateDataSource {

    override val latestExchangeRate: Flow<ExchangeRate> = flow {
        while (true) {
            val exchangeRate = mockApi.getCurrentExchangeRate()

            Timber.d("Emitting new exchange rate")
            emit(exchangeRate)

            delay(1_000)
        }
    }

}