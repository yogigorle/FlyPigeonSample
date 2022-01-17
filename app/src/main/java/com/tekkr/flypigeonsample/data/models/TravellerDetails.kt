package com.tekkr.flypigeonsample.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravellerDetails(
    val id: Int,
    val passengerType: String,
    val firstName: String,
    val lastName: String,
    val dob: String = "",
    val passportNumber: String = "",
    val passportExpiryDate: String = "",
    val passportIssueCountry: String = "India",
    val gender: String,
    val title: String
) : Parcelable

