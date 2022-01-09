package com.tekkr.flypigeonsample.data.models

data class TravellerDetails(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val dob: String = "",
    val passportNumber: String = "",
    val passportExpiryDate: String = "",
    val passportIssueCountry: String = "India",
    val gender: String
)

