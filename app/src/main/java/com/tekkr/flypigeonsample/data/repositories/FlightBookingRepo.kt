package com.tekkr.flypigeonsample.data.repositories

import com.google.gson.JsonObject
import com.tekkr.flypigeonsample.data.models.BookingDetails
import com.tekkr.flypigeonsample.data.network.ApiInterface
import com.tekkr.flypigeonsample.data.network.SafeApiRequest

class FlightBookingRepo(
    private val apiInterface: ApiInterface
) : SafeApiRequest() {

    suspend fun getFareRules(fareSourceCode: String) = executeApiCall {
        apiInterface.getFareRules(fareSourceCode)
    }

    suspend fun verifyPayment(body: JsonObject) = executeApiCall {
        apiInterface.verifyPayment(body)
    }

    suspend fun bookFlight(bookingDetails: BookingDetails) = executeApiCall {
        apiInterface.bookFlight(bookingDetails)
    }

    suspend fun getTripDetails(bookingUniqueId: String) = executeApiCall {
        apiInterface.getTripDetails(bookingUniqueId)
    }



}