package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName

class AirportsData(
    val id: String,
    @field:SerializedName(
        "AirportCode"
    )
    var airportcode: String,
    @field:SerializedName("AirportName") var airportname: String,
    @field:SerializedName(
        "City"
    ) var city: String,
    @field:SerializedName("Country") var country: String
)
