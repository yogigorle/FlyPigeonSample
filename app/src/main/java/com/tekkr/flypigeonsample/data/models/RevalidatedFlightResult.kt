package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName


data class RevalidatedFlightResult(
    val AirRevalidateResponse: RevalidatedFlightResponse
) {
    data class RevalidatedFlightResponse(
        val AirRevalidateResult: RevalidatedFlightResult
    ) {
        data class RevalidatedFlightResult(
            val ExtraServices: RevalidatedExtraServices,
            val FareItineraries: RevalidatedFareItinerary,
            val IsValid: String
        ) {
            data class RevalidatedExtraServices(
                val Services: List<Service>
            ) {
                data class Service(
                    val Service: FlightService
                ) {
                    data class FlightService(
                        val Behavior: String,
                        val CheckInType: String,
                        val Description: String,
                        val FlightDesignator: String,
                        val IsMandatory: Boolean,
                        val Relation: String,
                        val ServiceCost: FlightServiceCost,
                        val ServiceId: String,
                        val Type: String
                    ) {
                        data class FlightServiceCost(
                            val Amount: Int,
                            val CurrencyCode: String,
                            val DecimalPlaces: String
                        )
                    }
                }
            }
        }
    }
}