package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import com.lukaslechner.coroutineusecasesonandroid.utils.EndpointShouldNotBeCalledException
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.mockito.Mock
import retrofit2.HttpException
import retrofit2.Response

class FakeFeaturesErrorApi:MockApi {
    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        return when(apiLevel){
            27 -> mockVersionFeaturesOreo
            28 -> mockVersionFeaturesPie
            29 -> throw HttpException(
                Response.error<List<AndroidVersion>>(
                    500,
                    ResponseBody.create(MediaType.parse("application/json"),"")
                )
            )
            else -> throw java.lang.IllegalArgumentException("apiLevel not found")
        }
    }
}