package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class RevalidateFlightResult(
    @SerializedName("AirRevalidateResponse")
    val airRevalidateResponse: AirRevalidateResponse
) {
    data class AirRevalidateResponse(
        @SerializedName("AirRevalidateResult")
        val airRevalidateResult: AirRevalidateResult
    ) {
        @Parcelize
        data class AirRevalidateResult(
            @SerializedName("ExtraServices")
            val extraServices: ExtraServices,
            @SerializedName("FareItineraries")
            val fareItineraries: RevalidatedFareItinerary,
            @SerializedName("IsValid")
            val isValid: Boolean
        ) : Parcelable {
            @Parcelize
            data class ExtraServices(
                val Services: List<Service>
            ) : Parcelable {
                @Parcelize
                data class Service(
                    val behaviour: String,
                    val checkInType: String,
                    val description: String,
                    val flightDesignator: String,
                    val isMandatory: Boolean,
                    val relation: String,
                    val serviceCost: ServiceCost,
                    val serviceId: String,
                    val type: String
                ) : Parcelable {
                    @Parcelize
                    data class ServiceCost(
                        val amount: Int,
                        val currencyCode: String
                    ) : Parcelable
                }
            }
        }




    }
}