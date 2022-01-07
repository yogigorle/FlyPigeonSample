package com.tekkr.flypigeonsample.data.models

data class TravellerDetails(
    val title: String,
    val firstName: String,
    val lastName: String,
    val dob: String = "",
    val passportNumber: String = "",
    val passengerType: String,
    val passportExpiryDate: String = "",
    val passportIssueCountry: String = "India",
    val gender: String
)

