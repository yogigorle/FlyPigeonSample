package com.tekkr.flypigeonsample.utils

object Constants {
    const val origin = "Origin"
    const val destination = "Destination"
    const val fareItinerary = "FARE_ITINERARY"
    const val fareSourceCode = "FARE_SOURCE_CODE"
    const val roundTripTotalFare = "Round_trip_total_fare"
    const val bookingUniqueId = "BOOKING_UNIQUE_ID"

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

    enum class FlightTypes(val type: String) {
        OneWay("one_way"),
        Return("round_trip"),
        Departure("departure"),
        Arrival("arrival")
    }

    enum class Gender(val shortName: String){
        Male("M"),
        Female("F")
    }


}