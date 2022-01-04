package com.tekkr.flypigeonsample.data.network

import com.tekkr.flypigeonsample.data.models.FlightsSearchResult
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("api/flights/FlightSearchApi/")
    suspend fun getFlightSearchResults(
        @QueryMap queries: HashMap<String, String>
    ): FlightsSearchResult

}