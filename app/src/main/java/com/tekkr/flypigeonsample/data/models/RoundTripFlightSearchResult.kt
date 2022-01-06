package com.tekkr.flypigeonsample.data.models

data class RoundTripFlightSearchResult(
    val AirSearchResponse: FlightsSearchResponse
) {
    data class FlightsSearchResponse(
        val AirSearchResult: FlightSearchInfo
    ) {
        data class FlightSearchInfo(
            val FareItineraries: List<List<AirFareItinerary>>
        )
    }
}
