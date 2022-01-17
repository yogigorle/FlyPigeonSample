package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName

data class FlightTripDetails(
    @SerializedName("TripDetailsResponse")
    val tripDetailsResponse: TripDetailsResponse
) {
    data class TripDetailsResponse(
        @SerializedName("TripDetailsResult")
        val tripDetailsResult: TripDetailsResult
    ) {
        data class TripDetailsResult(
            val Success: String,
            val Target: String,
            @SerializedName("TravelItinerary")
            val travelItinerary: TravelItinerary
        ) {
            data class TravelItinerary(
                val BookingStatus: String,
                val CrossBorderIndicator: Any,
                val Destination: String,
                val FareType: String,
                val IsCommissionable: Any,
                val IsMOFare: Any,
                @SerializedName("ItineraryInfo")
                val itineraryInfo: ItineraryInfo,
                val Origin: String,
                val TicketStatus: String,
                val UniqueID: String
            ) {
                data class ItineraryInfo(
                    val CustomerInfos: List<CustomerInfo>,
                    @SerializedName("ItineraryPricing")
                    val itineraryPricing: ItineraryPricing,
                    val ReservationItems: List<ReservationItem>,
                    val TripDetailsPTC_FareBreakdowns: List<TripDetailsPTCFareBreakdown>
                ) {
                    data class CustomerInfo(
                        @SerializedName("CustomerInfo")
                        val customerInfo: CustomerInfo
                    ) {
                        data class CustomerInfo(
                            val DateOfBirth: String,
                            val EmailAddress: String,
                            val Gender: String,
                            val ItemRPH: String,
                            val PassengerFirstName: String,
                            val PassengerLastName: String,
                            val PassengerNationality: String,
                            val PassengerTitle: String,
                            val PassengerType: String,
                            val PassportNumber: String,
                            val PhoneNumber: String,
                            val PostCode: String,
                            val eTicketNumber: String
                        )
                    }

                    data class ItineraryPricing(
                        @SerializedName("TotalFare")
                        val totalFare: TotalFare
                    ) {

                        data class TotalFare(
                            val Amount: Double,
                            val CurrencyCode: String,
                            val DecimalPlaces: Int
                        )
                    }

                    data class ReservationItem(
                        @SerializedName("ReservationItem")
                        val reservationItem: ReservationItem
                    ) {
                        data class ReservationItem(
                            val AirlinePNR: String,
                            val ArrivalAirportLocationCode: String,
                            val ArrivalDateTime: String,
                            val ArrivalTerminal: String,
                            val Baggage: Any,
                            val CabinClassText: Int,
                            val DepartureAirportLocationCode: String,
                            val DepartureDateTime: String,
                            val DepartureTerminal: String,
                            val FlightNumber: String,
                            val ItemRPH: Any,
                            val JourneyDuration: Int,
                            val MarketingAirlineCode: String,
                            val NumberInParty: Int,
                            val OperatingAirlineCode: String,
                            val ResBookDesigCode: Any,
                            val StopQuantity: Int
                        )
                    }

                    data class TripDetailsPTCFareBreakdown(
                        val TripDetailsPTC_FareBreakdown: TripDetailsPTCFareBreakdown
                    ) {
                        data class TripDetailsPTCFareBreakdown(
                            @SerializedName("PassengerTypeQuantity")
                            val passengerTypeQuantity: PassengerTypeQuantity,
                            @SerializedName("TripDetailsPassengerFare")
                            val tripDetailsPassengerFare: TripDetailsPassengerFare
                        ) {
                            data class PassengerTypeQuantity(
                                val Code: String,
                                val Quantity: Int
                            )

                            data class TripDetailsPassengerFare(
                                @SerializedName("TotalFare")
                                val totalFare: TotalFare
                            ) {
                                data class TotalFare(
                                    val Amount: Double,
                                    val CurrencyCode: String,
                                    val DecimalPlaces: Int
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}