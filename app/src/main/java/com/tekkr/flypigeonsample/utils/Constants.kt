package com.tekkr.flypigeonsample.utils

object Constants {
    const val origin = "Origin"
    const val destination = "Destination"
    const val oneWay = "OneWay"
    const val roundTrip = "RoundTrip"

    enum class FlightSearchQueryParams(val param: String) {
        journeyType("journey_type"),
        srcAirPortCode("airport_from_code"),
        destAirPortCode("airport_to_code"),
        depDate("departure_date"),
        returnDate("return_date"),
        adultsCount("adult_flight"),
        childrenCount("child_flight"),
        infantsCount("infant_flight"),
        flightClass("fclass"),
        srcCity("src_city"),
        destCity("dest_city"),
        formattedDepDate("formatted_dep_date"),

    }
}