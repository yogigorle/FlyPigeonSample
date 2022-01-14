package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightDetailsForReview(
    val flightSrcAndDest: String,
    val selectedClass: String,
    val revalidatedFlightResult: RevalidateFlightResult
) : Parcelable {

    val revalidatedFareItinerary: RevalidatedFareItinerary
        get() = revalidatedFlightResult.searchData.airRevalidateResponse.airRevalidateResult.fareItineraries.fareItinerary

    val journeyOverView: String
        get() = "$selectedClass | ${revalidatedFareItinerary.originDestinationOptions[0].stopInfo} | ${revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.formattedFlightDuration}"

    val srcAirportInfo: String
        get() = "${flightSrcAndDest.substringBefore("to")} ${revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.DepartureAirportLocationCode}"

    val destAirportInfo: String
        get() = "${flightSrcAndDest.substringAfter("to")} ${revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.ArrivalAirportLocationCode}"

    val flightName: String
        get() = revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.operatingAirline.formattedFlightCode

    val depTime: String
        get() = revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.formattedDepTime

    val arrTime: String
        get() = revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.formattedArrTime

    val depDate: String
        get() = revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.readableDepDate

    val arrDate: String
        get() = revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.readableArrDate

    val flightDuration: String
        get() = revalidatedFareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.formattedFlightDuration

    val refundableStatus: Boolean
        get() = revalidatedFareItinerary.airItineraryFareInfo.IsRefundable

    val flightFare: String
        get() = revalidatedFareItinerary.airItineraryFareInfo.fareBreakdown[0].passengerFare.totalFare.formattedTotalFare

    val razorPayOrderId: String
        get() = revalidatedFlightResult.orderData.r_order_id

}