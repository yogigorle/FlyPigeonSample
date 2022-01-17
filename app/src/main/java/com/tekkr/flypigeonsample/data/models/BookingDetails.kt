package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingDetails(
    val emailId: String,
    val mobileNum: String,
    val countryCode: String,
    val customerType: String,
    val isPassPortMandatory: Boolean,
    val adultCount: Int,
    val childCount: Int,
    val infantCount: Int,
    val pinCode: String,
    val fareSourceCode: String,
    val bookingCustomer: List<TravellerDetails>

) : Parcelable