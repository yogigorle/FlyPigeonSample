package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName

data class RevalidateFlightResult(
    @SerializedName("AirRevalidateResponse")
    val airRevalidateResponse: AirRevalidateResponse
) {
    data class AirRevalidateResponse(
        @SerializedName("AirRevalidateResult")
        val airRevalidateResult: AirRevalidateResult
    ) {
        data class AirRevalidateResult(
            @SerializedName("ExtraServices")
            val extraServices: ExtraServices,
            val FareItineraries: Any,
            val IsValid: String
        ) {
            data class ExtraServices(
                val Services: List<Any>
            )
        }
    }
}