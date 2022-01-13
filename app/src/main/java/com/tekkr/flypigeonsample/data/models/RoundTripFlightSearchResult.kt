package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tekkr.flypigeonsample.utils.convertToHoursAndMins
import com.tekkr.flypigeonsample.utils.formatFlightDate
import com.tekkr.flypigeonsample.utils.formatFlightTime
import com.tekkr.flypigeonsample.utils.formatMoney
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class RoundTripFlightSearchResult(
    val AirSearchResponse: FlightsSearchResponse
) {
    data class FlightsSearchResponse(
        val AirSearchResult: FlightSearchInfo
    ) {
        data class FlightSearchInfo(
            val FareItineraries: List<List<RoundTripAirFareItinerary>>
        )
    }
}


@Parcelize
data class RoundTripAirFareItinerary(

    @SerializedName("FareItinerary")
    val fareItinerary: FareItinerary,
    val DirectionInd: String,
    val IsPassportMandatory: Boolean,
    val OriginDestinationOptions: List<OriginDestinationOption>,
    val TicketType: String,
    val ValidatingAirlineCode: String
) : Parcelable {

    @Parcelize
    data class FareItinerary(
        @SerializedName("AirItineraryFareInfo")
        val airItineraryFareInfo: AirItineraryFareInfo,
    ) : Parcelable

    @Parcelize
    data class AirItineraryFareInfo(
        val DivideInPartyIndicator: Boolean,
        @SerializedName("FareBreakdown")
        val fareBreakdown: FareBreakdown,
        val FareInfos: @RawValue List<Any>,
        val FareSourceCode: String,
        val FareType: String,
        val IsRefundable: Boolean,
        @SerializedName("ItinTotalFares")
        val itinTotalFares: ItinTotalFares,
        val ResultIndex: String,
        val SplitItinerary: Boolean
    ) : Parcelable {
        @Parcelize
        data class FareBreakdown(
            val FareBasisCode: String,
            @SerializedName("PassengerFare")
            val passengerFare: PassengerFare,
            @SerializedName("PassengerTypeQuantity")
            val passengerTypeQuantity: PassengerTypeQuantity
        ) : Parcelable {
            @Parcelize
            data class PassengerFare(
                @SerializedName("TotalFare")
                val totalFare: TotalFare
            ) : Parcelable {
                @Parcelize
                data class TotalFare(
                    val Amount: Int,
                    val CurrencyCode: String,
                    val DecimalPlaces: String
                ) : Parcelable {
                    val formattedTotalFare: String
                        get() = Amount.formatMoney()
                }
            }

            @Parcelize
            data class PassengerTypeQuantity(
                val Code: String,
                val Quantity: Int
            ) : Parcelable
        }

        @Parcelize
        data class ItinTotalFares(
            @SerializedName("TotalFare")
            val totalFare: TotalFare,

            ) : Parcelable {

            @Parcelize
            data class TotalFare(
                val Amount: String,
                val CurrencyCode: String,
                val DecimalPlaces: String
            ) : Parcelable

        }
    }

    @Parcelize
    data class OriginDestinationOption(
        @SerializedName("FlightSegment")
        val flightSegment: FlightSegment,
        @SerializedName("SeatsRemaining")
        val seatsRemaining: SeatsRemaining
    ) : Parcelable {
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
        ) : Parcelable {

            val readableDepDate: String
                get() = DepartureDateTime.formatFlightDate()

            val readableArrDate: String
                get() = ArrivalDateTime.formatFlightDate()

            val formattedDepTime: String
                get() = DepartureDateTime.formatFlightTime()

            val formattedArrTime: String
                get() = ArrivalDateTime.formatFlightTime()

            val formattedFlightDuration: String
                get() = if (JourneyDuration.toInt() > 60) JourneyDuration.toInt().convertToHoursAndMins() else "$JourneyDuration m"

            @Parcelize
            data class OperatingAirline(
                val Code: String,
                val FlightNumber: String
            ) : Parcelable {
                val formattedFlightCode: String
                    get() = "$Code $FlightNumber"
            }

        }

        @Parcelize
        data class SeatsRemaining(
            val BelowMinimum: String,
            val Number: Int
        ) : Parcelable

    }
}
