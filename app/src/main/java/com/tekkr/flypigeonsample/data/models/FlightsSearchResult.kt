package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName

data class FlightsSearchResult(
    val AirSearchResponse: FlightsSearchResponse
) {
    data class FlightsSearchResponse(
        val AirSearchResult: FlightSearchInfo
    ) {
        data class FlightSearchInfo(
            val FareItineraries: List<FareItinerary>
        ) {
            data class FareItinerary(
                @SerializedName("FareItinerary")
                val fareItinerary: FareItinerary
            ) {
                data class FareItinerary(
                    @SerializedName("AirItineraryFareInfo")
                    val airItineraryFareInfo: AirItineraryFareInfo,
                    val DirectionInd: String,
                    val IsPassportMandatory: Boolean,
                    val OriginDestinationOptions: List<OriginDestinationOption>,
                    val SequenceNumber: Any,
                    val TicketType: String,
                    val ValidatingAirlineCode: String
                ) {
                    data class AirItineraryFareInfo(
                        val DivideInPartyIndicator: Boolean,
                        @SerializedName("FareBreakdown")
                        val fareBreakdown: List<FareBreakdown>,
                        val FareInfos: List<Any>,
                        val FareSourceCode: String,
                        val FareType: String,
                        val IsRefundable: Boolean,
                        @SerializedName("ItinTotalFares")
                        val itinTotalFares: ItinTotalFares,
                        val ResultIndex: String,
                        val SplitItinerary: Boolean
                    ) {
                        data class FareBreakdown(
                            val FareBasisCode: String,
                            @SerializedName("PassengerFare")
                            val passengerFare: PassengerFare,
                            @SerializedName("PassengerTypeQuantity")
                            val passengerTypeQuantity: PassengerTypeQuantity
                        ) {
                            data class PassengerFare(
                                @SerializedName("TotalFare")
                                val totalFare: TotalFare
                            ) {
                                data class TotalFare(
                                    val Amount: Int,
                                    val CurrencyCode: String,
                                    val DecimalPlaces: String
                                )
                            }

                            data class PassengerTypeQuantity(
                                val Code: String,
                                val Quantity: Int
                            )
                        }

                        data class ItinTotalFares(
                            @SerializedName("TotalFare")
                            val totalFare: TotalFare,

                            ) {

                            data class TotalFare(
                                val Amount: String,
                                val CurrencyCode: String,
                                val DecimalPlaces: String
                            )

                        }
                    }

                    data class OriginDestinationOption(
                        @SerializedName("OriginDestinationOption")
                        val originDestinationOption: List<OriginDestinationOption>,
                        val TotalStops: Int
                    ) {
                        data class OriginDestinationOption(
                            @SerializedName("FlightSegment")
                            val flightSegment: FlightSegment,
                            val ResBookDesigCode: Any,
                            val ResBookDesigText: Any,
                            @SerializedName("SeatsRemaining")
                            val seatsRemaining: SeatsRemaining,
                            val StopQuantity: Int,
                            @SerializedName("StopQuantityInfo")
                            val stopQuantityInfo: StopQuantityInfo
                        ) {
                            data class FlightSegment(
                                val ArrivalAirportLocationCode: String,
                                val ArrivalDateTime: String,
                                val CabinClassCode: String,
                                val CabinClassText: String,
                                val DepartureAirportLocationCode: String,
                                val DepartureDateTime: String,
                                val Eticket: Boolean,
                                val FlightNumber: String,
                                val JourneyDuration: String,
                                val MarketingAirlineCode: String,
                                val MarriageGroup: Any,
                                val MealCode: Any,
                                @SerializedName("OperatingAirline")
                                val operatingAirline: OperatingAirline,
                                val distanceBetweenAirports: String,
                                val distanceUnit: String
                            ) {
                                data class OperatingAirline(
                                    val Code: String,
                                    val Equipment: Any,
                                    val FlightNumber: String
                                )
                            }

                            data class SeatsRemaining(
                                val BelowMinimum: String,
                                val Number: Int
                            )

                            data class StopQuantityInfo(
                                val ArrivalDateTime: String,
                                val DepartureDateTime: String,
                                val Duration: Int,
                                val LocationCode: String
                            )
                        }
                    }
                }
            }
        }
    }
}