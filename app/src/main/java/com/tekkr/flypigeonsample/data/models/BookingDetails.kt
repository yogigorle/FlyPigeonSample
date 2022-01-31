package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class BookingDetails(
    val FareSourceCode: String,
    val IsPassportMandatory: String,
    val PostCode: String,
    val adult_flight: Int,
    val area_code: String,
    val booking_customer: List<JsonObject>,
    val c_type: String,
    val child_flight: Int,
    val country_code: String,
    val email_id: String,
    val infant_flight: Int,
    val mobile_no: String,
    val razorpay_order_id: String,
    val flightType: String

)