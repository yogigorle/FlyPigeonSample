package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class RevalidateFlightResult(
    @SerializedName("search_data")
    val searchData: RevalidatedFlightSearchData,
    @SerializedName("razor_order_data")
    val orderData: OrderData
) {

    data class OrderData(
        val id: Int,
        val fp_booking_order_id: String,
        val r_order_id: String,
        val r_amount: Int,
        val r_currency: String,
        val r_receipt: String,
        val created_at: String
    )

    data class RevalidatedFlightSearchData(
        @SerializedName("AirRevalidateResponse")
        val airRevalidateResponse: AirRevalidateResponse
    )

    data class AirRevalidateResponse(
        @SerializedName("AirRevalidateResult")
        val airRevalidateResult: AirRevalidateResult
    ) {
        @Parcelize
        data class AirRevalidateResult(
            @SerializedName("ExtraServices")
            val extraServices: ExtraServices,
            @SerializedName("FareItineraries")
            val fareItineraries: FareItinerary,
            @SerializedName("IsValid")
            val isValid: String
        ) : Parcelable {
            @Parcelize
            data class ExtraServices(
                val Services: List<Service>
            ) : Parcelable {
                @Parcelize
                data class Service(
                    val Service: ServicesOffered

                ) : Parcelable {
                    @Parcelize
                    data class ServicesOffered(
                        @SerializedName("Behavior")
                        val behaviour: String,
                        @SerializedName("CheckInType")
                        val checkInType: String,
                        @SerializedName("Description")
                        val description: String,
                        @SerializedName("FlightDesignator")
                        val flightDesignator: String,
                        @SerializedName("IsMandatory")
                        val isMandatory: Boolean,
                        @SerializedName("Relation")
                        val relation: String,
                        @SerializedName("ServiceCost")
                        val serviceCost: ServiceCost,
                        @SerializedName("ServiceId")
                        val serviceId: String,
                        @SerializedName("Type")
                        val type: String
                    ) : Parcelable

                    @Parcelize
                    data class ServiceCost(
                        @SerializedName("Amount")
                        val amount: Int,
                        @SerializedName("CurrencyCode")
                        val currencyCode: String
                    ) : Parcelable
                }
            }

            @Parcelize
            data class FareItinerary(
                @SerializedName("FareItinerary")
                val fareItinerary: RevalidatedFareItinerary
            ) : Parcelable
        }

    }
}