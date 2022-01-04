package com.tekkr.flypigeonsample.data.repositories

import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.network.SafeApiRequest

class FlightsSearchRepo(
    private val apiService: ApiInterface
) : SafeApiRequest() {

    suspend fun getFlightsSearchResults(queryParams: HashMap<String, String>) = executeApiCall {
        apiService.getFlightSearchResults(queryParams)
    }



}