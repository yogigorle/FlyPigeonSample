package com.tekkr.flypigeonsample.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekkr.flypigeonsample.data.models.FareRulesResult
import com.tekkr.flypigeonsample.data.network.Resource
import com.tekkr.flypigeonsample.data.repositories.FlightBookingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightBookingViewModel @Inject constructor(
    private val flightBookingRepo: FlightBookingRepo
) : ViewModel() {

    fun getFareRules(fareSourceCode: String): LiveData<Resource<FareRulesResult>> {
        val response: MutableLiveData<Resource<FareRulesResult>> = MutableLiveData()
        viewModelScope.launch {
            response.postValue(Resource.Loading)
            response.postValue(flightBookingRepo.getFareRules(fareSourceCode))
        }

        return response
    }

}