package com.tekkr.flypigeonsample.data.network

import com.tekkr.flypigeonsample.data.models.OneWayFlightsSearchResult
import com.tekkr.flypigeonsample.data.models.RoundTripFlightSearchResult
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("api/flights/FlightSearchApi/")
    suspend fun getOneWayFlightSearchResults(
        @QueryMap queries: HashMap<String, String>
    ): OneWayFlightsSearchResult

    @GET("api/flights/FlightSearchApi/")
    suspend fun getRoundTripFlightSearchResults(
        @QueryMap queries: HashMap<String, String>
    ): RoundTripFlightSearchResult

}