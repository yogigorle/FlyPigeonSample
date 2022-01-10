package com.tekkr.flypigeonsample.utils

object Constants {
    const val origin = "Origin"
    const val destination = "Destination"
    const val fareItinerary = "FARE_ITINERARY"
    const val sourceAnDestCity = "source_to_dest_city"

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
        RoundTrip("RoundTrip"),

    }

    enum class TravellerType(val type: String) {
        Adult("adult"),
        Child("child"),
        Infant("infant"),
    }


}