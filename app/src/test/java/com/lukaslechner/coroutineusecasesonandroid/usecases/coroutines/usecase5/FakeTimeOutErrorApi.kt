package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.utils.EndpointShouldNotBeCalledException
import kotlinx.coroutines.delay
import org.mockito.internal.matchers.And

class FakeTimeOutErrorApi(val timeOut:Long): MockApi {
    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        delay(timeOut)
      return  emptyList<AndroidVersion>()
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }
}