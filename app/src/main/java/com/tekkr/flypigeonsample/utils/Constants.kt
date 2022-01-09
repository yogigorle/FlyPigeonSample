package com.tekkr.flypigeonsample.utils

object Constants {
    const val origin = "Origin"
    const val destination = "Destination"
    const val oneWay = "OneWay"
    const val roundTrip = "RoundTrip"
    const val fareItinerary = "FARE_ITINERARY"
    const val sourceAnDestCity = "source_to_dest_city"

    enum class FlightSearchQueryParams(val param: String) {
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
        DestCity("dest_city"),
        FormattedDepDate("formatted_dep_date"),

    }

    enum class TravellerType(val type: String){
        Adult("adult"),
        Child("child"),
        Infant("infant"),
    }



}