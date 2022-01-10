package com.tekkr.flypigeonsample.data.repositories

import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.network.SafeApiRequest
import java.security.CodeSource

class FlightsSearchRepo(
    private val apiService: ApiInterface
) : SafeApiRequest() {

    suspend fun getAirportsList(queryParams: String) = executeApiCall {
        apiService.getAirportsList(queryParams)
    }


    suspend fun getOneWayFlightsSearchResults(queryParams: HashMap<String, String>) =
        executeApiCall {
            apiService.getOneWayFlightSearchResults(queryParams)
        }

    suspend fun getRoundTripFlightsSearchResults(queryParams: HashMap<String, String>) =
        executeApiCall {
            apiService.getRoundTripFlightSearchResults(queryParams)
        }

    suspend fun revalidateFlight(fareSourceCode: String) = executeApiCall {
        apiService.revalidateFlight(fareSourceCode)
    }

}