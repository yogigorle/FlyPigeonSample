package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName

data class RoundTripF(
    @SerializedName("AirSearchResponse")
    val airSearchResponse: AirSearchResponse
) {
    data class AirSearchResponse(
        @SerializedName("AirSearchResult")
        val airSearchResult: AirSearchResult
    ) {
        data class AirSearchResult(
            val FareItineraries: List<List<FareItinerary>>
        ) {
            data class FareItinerary(
                val DirectionInd: String,
                @SerializedName("FareItinerary")
                val fareItinerary: FareItinerary,
                val IsPassportMandatory: Boolean,
                val OriginDestinationOptions: List<Any>,
                val SequenceNumber: Any,
                val TicketType: String,
                val ValidatingAirlineCode: String
            ) {
                data class FareItinerary(
                    @SerializedName("AirItineraryFareInfo")
                    val airItineraryFareInfo: AirItineraryFareInfo
                ) {
                    data class AirItineraryFareInfo(
                        val DivideInPartyIndicator: Boolean,
                        @SerializedName("FareBreakdown")
                        val fareBreakdown: FareBreakdown,
                        val FareInfos: List<Any>,
                        val FareSourceCode: String,
                        val FareType: String,
                        val IsRefundable: Boolean,
                        val ResultIndex: String,
                        val SplitItinerary: Any
                    ) {
                        data class FareBreakdown(
                            val FareBasisCode: String
                        ) {

                        }

                    }
                }

                data class OriginDestinationOption(
                    @SerializedName("FlightSegment")
                    val flightSegment: FlightSegment,
                    val ResBookDesigCode: Any,
                    val ResBookDesigText: Any,
                    @SerializedName("SeatsRemaining")
                    val seatsRemaining: SeatsRemaining,
                    val StopQuantity: Any,
                    @SerializedName("StopQuantityInfo")
                    val stopQuantityInfo: StopQuantityInfo
                ) {
                    data class FlightSegment(
                        val ArrivalAirportLocationCode: String,
                        val ArrivalDateTime: String,
                        val CabinClassCode: Any,
                        val CabinClassText: String,
                        val DepartureAirportLocationCode: String,
                        val DepartureDateTime: String,
                        val Eticket: Boolean,
                        val FlightNumber: String,
                        val MarketingAirlineCode: String,
                        val MarriageGroup: Any,
                        val MealCode: Any,
                        @SerializedName("OperatingAirline")
                        val operatingAirline: OperatingAirline
                    ) {
                        data class OperatingAirline(
                            val Code: String,
                            val Equipment: Any,
                            val FlightNumber: String
                        )
                    }

                    data class SeatsRemaining(
                        val BelowMinimum: String,
                        val Number: Any
                    )

                    data class StopQuantityInfo(
                        val ArrivalDateTime: Any,
                        val DepartureDateTime: Any,
                        val Duration: Int,
                        val LocationCode: Any
                    )
                }
            }
        }
    }
}