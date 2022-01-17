package com.tekkr.flypigeonsample.data.network

import com.google.gson.JsonObject
import com.tekkr.flypigeonsample.data.models.*
import retrofit2.http.*

interface ApiInterface {

    @GET("api/flights/FlightSearchApi/")
    suspend fun getOneWayFlightSearchResults(
        @QueryMap queries: HashMap<String, String>
    ): OneWayFlightsSearchResult

    @GET("api/flights/FlightSearchApi/")
    suspend fun getRoundTripFlightSearchResults(
        @QueryMap queries: HashMap<String, String>
    ): RoundTripFlightSearchResult

    @GET("api/flights/FlightRevalidateApi/")
    suspend fun revalidateFlight(
        @Query("fare_source_code") fareSourceCode: String
    ): RevalidateFlightResult

    @GET("api/flights/airportlist/")
    suspend fun getAirportsList(
        @Query("airport_search") airportSearchString: String
    ): List<AirportsData>

    @GET("api/flights/FlightFareRuleApi/")
    suspend fun getFareRules(
        @Query("fare_source_code") fareSourceCode: String
    ): FareRulesResult

    @POST("api/flights/FlightBookingCustomerAPI/")
    suspend fun bookFlight(
        @Body bookingDetails: BookingDetails
    ): FlightBookingResult

    @POST("api/payment/paymentverify/")
    suspend fun verifyPayment(
        @Body data: JsonObject
    ): PaymentVerificationResponse

    @GET("api/flights/FlightTripDetailsApi/")
    suspend fun getTripDetails(
        @Query("UniqueID") uniqueId: String
    ): FlightTripDetails

}