package com.tekkr.flypigeonsample.utils

object Constants {
    const val origin = "Origin"
    const val destination = "Destination"
    const val fareItinerary = "FARE_ITINERARY"
    const val revalidatedFlightResult = "REVALIDATED_FLIGHT_RESULT"
    const val sourceAnDestCity = "source_to_dest_city"
    const val fareSourceCode = "FARE_SOURCE_CODE"

    enum class FlightJourneyParams(val param: String) {
        JourneyType("journey_type"),
        SrcAirPortCode("airport_from_code"),
        DestAirPortCode("airport_to_code"),
        DepDate("departure_date"),
        ReturnDate("return_date"),
        AdultsCount("adult_flight"),
        ChildrenCount("child_flight"),
        InfantsCount("infant_flight"),
        FlightClass("fclass"),
        SrcCity("src_city"),
        SrcAirportName("src_airport_name"),
        DestAirportName("dest_airport_name"),
        DestCity("dest_city"),
        OneWay("OneWay"),
        FormattedDepDate("formatted_dep_date"),
        Return("Return"),

    }

    enum class TravellerType(val type: String) {
        Adult("adult"),
        Child("child"),
        Infant("infant"),
    }


}