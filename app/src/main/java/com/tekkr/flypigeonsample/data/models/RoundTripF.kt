package com.tekkr.flypigeonsample.data.models

//data class RoundTripF(
//    val AirSearchResponse: AirSearchResponse
//) {
//    data class AirSearchResponse(
//        val AirSearchResult: AirSearchResult
//    ) {
//        data class AirSearchResult(
//            val FareItineraries: List<List<FareItinerary>>
//        ) {
//            data class FareItinerary(
//                val DirectionInd: String,
//                val FareItinerary: FareItinerary,
//                val IsPassportMandatory: Boolean,
//                val OriginDestinationOptions: List<OriginDestinationOption>,
//                val SequenceNumber: Any,
//                val TicketType: String,
//                val ValidatingAirlineCode: String
//            ) {
//                data class FareItinerary(
//                    val AirItineraryFareInfo: AirItineraryFareInfo
//                ) {
//                    data class AirItineraryFareInfo(
//                        val DivideInPartyIndicator: Boolean,
//                        val FareBreakdown: FareBreakdown,
//                        val FareInfos: List<Any>,
//                        val FareSourceCode: String,
//                        val FareType: String,
//                        val IsRefundable: Boolean,
//                        val ItinTotalFares: ItinTotalFares,
//                        val ResultIndex: String,
//                        val SplitItinerary: Any
//                    ) {
//                        data class FareBreakdown(
//                            val FareBasisCode: String,
//                            val PassengerFare: PassengerFare
//                        ) {
//                            data class PassengerFare(
//                                val BaseFare: BaseFare,
//                                val EquivFare: EquivFare,
//                                val ServiceTax: ServiceTax,
//                                val Tax: List<Tax>,
//                                val TotalFare: TotalFare
//                            ) {
//                                data class BaseFare(
//                                    val Amount: Int,
//                                    val CurrencyCode: String,
//                                    val DecimalPlaces: Int
//                                )
//
//                                data class EquivFare(
//                                    val Amount: Int,
//                                    val CurrencyCode: String,
//                                    val DecimalPlaces: Int
//                                )
//
//                                data class ServiceTax(
//                                    val Amount: Int,
//                                    val CurrencyCode: String,
//                                    val DecimalPlaces: Int
//                                )
//
//                                data class Tax(
//                                    val key: String,
//                                    val value: Int
//                                )
//
//                                data class TotalFare(
//                                    val Amount: Int,
//                                    val CurrencyCode: String,
//                                    val DecimalPlaces: Int
//                                )
//                            }
//                        }
//
//                        data class ItinTotalFares(
//                            val BaseFare: BaseFare,
//                            val EquivFare: EquivFare,
//                            val ServiceTax: ServiceTax,
//                            val TotalFare: TotalFare,
//                            val TotalTax: TotalTax
//                        ) {
//                            data class BaseFare(
//                                val Amount: Int,
//                                val CurrencyCode: String,
//                                val DecimalPlaces: Int
//                            )
//
//                            data class EquivFare(
//                                val Amount: Int,
//                                val CurrencyCode: String,
//                                val DecimalPlaces: Int
//                            )
//
//                            data class ServiceTax(
//                                val Amount: Int,
//                                val CurrencyCode: String,
//                                val DecimalPlaces: Int
//                            )
//
//                            data class TotalFare(
//                                val Amount: Int,
//                                val CurrencyCode: String,
//                                val DecimalPlaces: Int
//                            )
//
//                            data class TotalTax(
//                                val Amount: Int,
//                                val CurrencyCode: String,
//                                val DecimalPlaces: Int
//                            )
//                        }
//                    }
//                }
//
//                data class OriginDestinationOption(
//                    val FlightSegment: FlightSegment,
//                    val ResBookDesigCode: Any,
//                    val ResBookDesigText: Any,
//                    val SeatsRemaining: SeatsRemaining,
//                    val StopQuantity: Any,
//                    val StopQuantityInfo: StopQuantityInfo
//                ) {
//                    data class FlightSegment(
//                        val ArrivalAirportLocationCode: String,
//                        val ArrivalDateTime: String,
//                        val CabinClassCode: Any,
//                        val CabinClassText: String,
//                        val DepartureAirportLocationCode: String,
//                        val DepartureDateTime: String,
//                        val Eticket: Boolean,
//                        val FlightNumber: String,
//                        val MarketingAirlineCode: String,
//                        val MarriageGroup: Any,
//                        val MealCode: Any,
//                        val OperatingAirline: OperatingAirline
//                    ) {
//                        data class OperatingAirline(
//                            val Code: String,
//                            val Equipment: Any,
//                            val FlightNumber: String
//                        )
//                    }
//
//                    data class SeatsRemaining(
//                        val BelowMinimum: String,
//                        val Number: Any
//                    )
//
//                    data class StopQuantityInfo(
//                        val ArrivalDateTime: Any,
//                        val DepartureDateTime: Any,
//                        val Duration: Int,
//                        val LocationCode: Any
//                    )
//                }
//            }
//        }
//    }
//}