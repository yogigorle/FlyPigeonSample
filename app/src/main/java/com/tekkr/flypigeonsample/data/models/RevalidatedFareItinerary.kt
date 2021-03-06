package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tekkr.flypigeonsample.utils.convertToHoursAndMins
import com.tekkr.flypigeonsample.utils.convertToReadableDate
import com.tekkr.flypigeonsample.utils.formatFlightTime
import com.tekkr.flypigeonsample.utils.formatMoney
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RevalidatedFareItinerary(
    @SerializedName("AirItineraryFareInfo")
    val airItineraryFareInfo: AirItineraryFareInfo,
    val DirectionInd: String,
    val IsPassportMandatory: Boolean,
    @SerializedName("OriginDestinationOptions")
    val originDestinationOptions: List<OriginDestinationOption>,
    val TicketType: Boolean,
    val ValidatingAirlineCode: String
) : Parcelable {
    @Parcelize
    data class AirItineraryFareInfo(
        val DivideInPartyIndicator: Boolean,
        @SerializedName("FareBreakdown")
        val fareBreakdown: List<FareBreakdown>,
        val FareSourceCode: String,
        val FareType: String,
        val IsRefundable: Boolean,
        @SerializedName("ItinTotalFares")
        val itinTotalFares: ItinTotalFares
    ) : Parcelable {
        @Parcelize
        data class FareBreakdown(
            @SerializedName("PassengerFare")
            val passengerFare: PassengerFare,
            @SerializedName("PassengerTypeQuantity")
            val passengerTypeQuantity: PassengerTypeQuantity
        ) : Parcelable {
            @Parcelize
            data class PassengerFare(
                @SerializedName("ServiceTax")
                val serviceTax: ServiceTax,
                @SerializedName("Taxes")
                val taxes: List<Taxes>,
                @SerializedName("TotalFare")
                val totalFare: TotalFare
            ) : Parcelable {
                @Parcelize
                data class ServiceTax(
                    val Amount: String,
                    val CurrencyCode: String,
                    val DecimalPlaces: String
                ) : Parcelable

                @Parcelize
                data class Taxes(
                    val Amount: String,
                    val CurrencyCode: String,
                    val DecimalPlaces: String,
                    val TaxCode: String
                ) : Parcelable

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
            @SerializedName("TotalTax")
            val totalTax: TotalTax
        ) : Parcelable {
            data class BaseFare(
                val Amount: String,
                val CurrencyCode: String,
                val DecimalPlaces: String
            )

            data class EquivFare(
                val Amount: String,
                val CurrencyCode: String,
                val DecimalPlaces: String
            )

            data class ServiceTax(
                val Amount: String,
                val CurrencyCode: String,
                val DecimalPlaces: String
            )

            @Parcelize
            data class TotalFare(
                val Amount: String,
                val CurrencyCode: String,
                val DecimalPlaces: String
            ) : Parcelable

            @Parcelize
            data class TotalTax(
                val Amount: String,
                val CurrencyCode: String,
                val DecimalPlaces: String
            ) : Parcelable
        }
    }

    @Parcelize
    data class OriginDestinationOption(
        @SerializedName("OriginDestinationOption")
        val originDestinationOption: List<OriginDestinationOptions>,
        val TotalStops: Int
    ) : Parcelable {

        val stopInfo: String
            get() = if (TotalStops > 0) "$TotalStops stop" else "non-stop"

        @Parcelize
        data class OriginDestinationOptions(
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
                    get() = DepartureDateTime.convertToReadableDate()

                val readableArrDate: String
                    get() = ArrivalDateTime.convertToReadableDate()

                val formattedDepTime: String
                    get() = DepartureDateTime.formatFlightTime()

                val formattedArrTime: String
                    get() = ArrivalDateTime.formatFlightTime()

                val formattedFlightDuration: String
                    get() = if (JourneyDuration.toInt() > 60) JourneyDuration.toInt()
                        .convertToHoursAndMins() else "$JourneyDuration m"

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
            ) : Parcelable
        }
    }
}