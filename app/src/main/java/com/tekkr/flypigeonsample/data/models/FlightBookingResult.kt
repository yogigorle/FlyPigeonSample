package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName

data class FlightBookingResult(
    val flight_data: FlightData
) {
    data class FlightData(
        @SerializedName("BookFlightResponse")
        val bookFlightResponse: BookFlightResponse
    ) {
        data class BookFlightResponse(
            @SerializedName("BookFlightResult")
            val bookFlightResult: BookFlightResult
        ) {
            data class BookFlightResult(
                @SerializedName("Errors")
                val errors: ErrorsFromServer? = null,
                val Status: String,
                val Success: String,
                val Target: String,
                val TktTimeLimit: String,
                val UniqueID: String
            ) {
                data class ErrorsFromServer(
                    @SerializedName("Errors")
                    val errors: Errors
                ) {
                    data class Errors(
                        val ErrorCode: String,
                        val ErrorMessage: String
                    )
                }
            }
        }
    }
}