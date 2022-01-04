package com.tekkr.flypigeonsample.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class SafeApiRequest {
    suspend fun <T> executeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> Resource.Failure(
                        false, throwable.code(), throwable.response()?.errorBody()
                    )
                    else -> Resource.Failure(true, null, null)
                }
            }
        }
    }
}