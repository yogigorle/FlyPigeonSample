package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tekkr.flypigeonsample.utils.convertToHoursAndMins
import com.tekkr.flypigeonsample.utils.convertToReadableDate
import com.tekkr.flypigeonsample.utils.formatFlightTime
import com.tekkr.flypigeonsample.utils.formatMoney
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class OneWayFlightsSearchResult(
    @SerializedName("AirSearchResponse")
    val airSearchResponse: FlightsSearchResponse
) {
    data class FlightsSearchResponse(
        @SerializedName("AirSearchResult")
        val airSearchResult: FlightSearchInfo
    ) {
        data class FlightSearchInfo(
            @SerializedName("FareItineraries")
            val fareItineraries: List<AirFareItinerary>
        )
    }
}

data class AirFareItinerary(
    @SerializedName("FareItinerary")
    val fareItinerary: @RawValue FareItinerary
) {
    @Parcelize
    data class FareItinerary(
        @SerializedName("AirItineraryFareInfo")
        val airItineraryFareInfo: AirItineraryFareInfo,
        val DirectionInd: String,
        val IsPassportMandatory: Boolean,
        val OriginDestinationOptions: List<OriginDestinationOption>,
        val TicketType: String,
        val ValidatingAirlineCode: String
    ): Parcelable {
        @Parcelize
        data class AirItineraryFareInfo(
            val DivideInPartyIndicator: Boolean,
            @SerializedName("FareBreakdown")
            val fareBreakdown: List<FareBreakdown>,
            val FareInfos: @RawValue List<Any>,
            val FareSourceCode: String,
            val FareType: String,
            val IsRefundable: Boolean,
            @SerializedName("ItinTotalFares")
            val itinTotalFares: ItinTotalFares,
            val ResultIndex: String,
            val SplitItinerary: Boolean
        ):Parcelable {
            @Parcelize
            data class FareBreakdown(
                val FareBasisCode: String,
                @SerializedName("PassengerFare")
                val passengerFare: PassengerFare,
                @SerializedName("PassengerTypeQuantity")
                val passengerTypeQuantity: PassengerTypeQuantity
            ): Parcelable {
                @Parcelize
                data class PassengerFare(
                    @SerializedName("TotalFare")
                    val totalFare: TotalFare
                ): Parcelable {
                    @Parcelize
                    data class TotalFare(
                        val Amount: Int,
                        val CurrencyCode: String,
                        val DecimalPlaces: String
                    ): Parcelable {
                        val formattedTotalFare: String
                            get() = Amount.formatMoney()
                    }
                }

                @Parcelize
                data class PassengerTypeQuantity(
                    val Code: String,
                    val Quantity: Int
                ): Parcelable
            }

            @Parcelize
            data class ItinTotalFares(
                @SerializedName("TotalFare")
                val totalFare: TotalFare,

                ): Parcelable {

                @Parcelize
                data class TotalFare(
                    val Amount: String,
                    val CurrencyCode: String,
                    val DecimalPlaces: String
                ): Parcelable

            }
        }

        @Parcelize
        data class OriginDestinationOption(
            @SerializedName("OriginDestinationOption")
            val originDestinationOption: List<OriginDestinationOptions>,
            val TotalStops: Int
        ): Parcelable {
            @Parcelize
            data class OriginDestinationOptions(
                @SerializedName("FlightSegment")
                val flightSegment: FlightSegment,
                @SerializedName("SeatsRemaining")
                val seatsRemaining: SeatsRemaining,
                val StopQuantity: Int,
                @SerializedName("StopQuantityInfo")
                val stopQuantityInfo: StopQuantityInfo
            ): Parcelable {
                @Parcelize
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
                    @SerializedName("OperatingAirline")
                    val operatingAirline: OperatingAirline,
                    val distanceBetweenAirports: String,
                    val distanceUnit: String
                ): Parcelable {
                    @Parcelize
                    data class OperatingAirline(
                        val Code: String,
                        val FlightNumber: String
                    ): Parcelable {
                        val formattedFlightCode: String
                            get() = "$Code $FlightNumber"
                    }

                }

                @Parcelize
                data class SeatsRemaining(
                    val BelowMinimum: String,
                    val Number: Int
                ): Parcelable

                @Parcelize
                data class StopQuantityInfo(
                    val ArrivalDateTime: String,
                    val DepartureDateTime: String,
                    val Duration: Int,
                    val LocationCode: String
                ): Parcelable {

                    val readableDepDate: String
                        get() = DepartureDateTime.convertToReadableDate()

                    val readableArrDate: String
                        get() = ArrivalDateTime.convertToReadableDate()

                    val formattedDepTime: String
                        get() = DepartureDateTime.formatFlightTime()

                    val formattedArrTime: String
                        get() = ArrivalDateTime.formatFlightTime()

                    val formattedFlightDuration: String
                        get() = if (Duration > 60) Duration.convertToHoursAndMins() else "$Duration m"
                }
            }
        }
    }
}