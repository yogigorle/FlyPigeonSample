package com.tekkr.flypigeonsample.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.salomonbrys.kotson.jsonObject
import com.tekkr.flypigeonsample.data.models.*
import com.tekkr.flypigeonsample.data.network.Resource
import com.tekkr.flypigeonsample.data.repositories.FlightBookingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightBookingViewModel @Inject constructor(
    private val flightBookingRepo: FlightBookingRepo
) : ViewModel() {

    val bookingDetails: MutableLiveData<BookingDetails> = MutableLiveData()

    fun getFareRules(fareSourceCode: String): LiveData<Resource<FareRulesResult>> {
        val response: MutableLiveData<Resource<FareRulesResult>> = MutableLiveData()
        viewModelScope.launch {
            response.postValue(Resource.Loading)
            response.postValue(flightBookingRepo.getFareRules(fareSourceCode))
        }

        return response
    }

    fun verifyPayment(
        rp_payment_id: String,
        rp_order_id: String,
        rp_signature: String
    ): LiveData<Resource<PaymentVerificationResponse>> {
        val response: MutableLiveData<Resource<PaymentVerificationResponse>> = MutableLiveData()
        viewModelScope.launch {
            response.postValue(Resource.Loading)
            response.postValue(
                flightBookingRepo.verifyPayment(
                    jsonObject(
                        "razorpay_payment_id" to rp_payment_id,
                        "razorpay_order_id" to rp_order_id,
                        "razorpay_signature" to rp_signature
                    )
                )
            )
        }

        return response
    }

    fun bookFlight(flightBookingDetails: BookingDetails): LiveData<Resource<FlightBookingResult>> {
        val response: MutableLiveData<Resource<FlightBookingResult>> = MutableLiveData()
        viewModelScope.launch {
            response.postValue(Resource.Loading)
            response.postValue(flightBookingRepo.bookFlight(flightBookingDetails))
        }

        return response
    }

    fun getTripDetails(bookingUniqueId: String): LiveData<Resource<FlightTripDetails>> {
        val response: MutableLiveData<Resource<FlightTripDetails>> = MutableLiveData()
        viewModelScope.launch {
            response.postValue(Resource.Loading)
            response.postValue(flightBookingRepo.getTripDetails(bookingUniqueId))
        }

        return response
    }


}