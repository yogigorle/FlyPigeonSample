package com.tekkr.flypigeonsample.data.repositories

import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.network.SafeApiRequest

class FlightBookingRepo(
    private val apiInterface: ApiInterface
) : SafeApiRequest() {

    suspend fun getFareRules(fareSourceCode: String) = executeApiCall {
        apiInterface.getFareRules(fareSourceCode)
    }

}