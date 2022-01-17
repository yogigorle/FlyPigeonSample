package com.tekkr.flypigeonsample.data.models

data class PaymentVerificationResponse(
    val payment_signature: String,
    val payment_status: String
)