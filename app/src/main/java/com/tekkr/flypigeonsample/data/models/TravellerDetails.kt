package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import com.tekkr.flypigeonsample.utils.convertMillsToDate
import com.tekkr.flypigeonsample.utils.formatFlightDate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravellerDetails(
    val id: Int,
    val passengerType: String,
    val firstName: String,
    val lastName: String,
    val dobInMillis: Long = 0L,
    val passportNumber: String = "",
    val passportExpiryDateInMillis: Long = 0L,
    val gender: String,
    val title: String
) : Parcelable {
    val formattedDob: String
        get() = dobInMillis.convertMillsToDate()

    val formattedPassportExpiryDate: String
        get() = passportExpiryDateInMillis.convertMillsToDate()
}

