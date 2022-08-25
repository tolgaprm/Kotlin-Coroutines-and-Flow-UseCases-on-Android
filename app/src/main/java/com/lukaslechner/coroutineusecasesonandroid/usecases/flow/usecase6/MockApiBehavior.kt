package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6

import android.content.Context
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.createFlowMockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.fakeCurrentExchangeRate
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.fakeCurrentStockPrices
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor

fun mockApi(context: Context) =
    createFlowMockApi(
        MockNetworkInterceptor()
            .mock(
                path = "/current-stock-prices",
                body = { Gson().toJson(fakeCurrentStockPrices(context)) },
                status = 200,
                delayInMs = 1000,
            )
            .mock(
                path = "/current-exchange-rate",
                body = { Gson().toJson(fakeCurrentExchangeRate()) },
                status = 200,
                delayInMs = 1000,
            )
    )