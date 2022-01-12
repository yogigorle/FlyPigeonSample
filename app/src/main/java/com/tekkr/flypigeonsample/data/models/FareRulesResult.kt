package com.tekkr.flypigeonsample.data.models

import com.google.gson.annotations.SerializedName

data class FareRulesResult(
    val FareRules1_1Response: FareRules11Response
) {
    data class FareRules11Response(
        val FareRules1_1Result: FareRules11Result
    ) {
        data class FareRules11Result(
            val BaggageInfos: List<BaggageInfo>,
            val FareRules: List<FareRule>
        ) {
            data class BaggageInfo(
                @SerializedName("BaggageInfo")
                val baggageInfo: BaggageInfo
            ) {
                data class BaggageInfo(
                    val Arrival: String,
                    val Baggage: Any,
                    val Departure: String,
                    val FlightNo: Int
                )
            }

            data class FareRule(
                @SerializedName("FareRule")
                val fareRule: FareRule
            ) {
                data class FareRule(
                    val Airline: String,
                    val Category: Any,
                    val CityPair: String,
                    val Rules: String
                )
            }
        }
    }
}