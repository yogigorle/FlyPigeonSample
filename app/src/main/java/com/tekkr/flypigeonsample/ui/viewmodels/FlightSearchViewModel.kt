package com.tekkr.flypigeonsample.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekkr.flypigeonsample.data.models.OneWayFlightsSearchResult
import com.tekkr.flypigeonsample.data.models.RoundTripFlightSearchResult
import com.tekkr.flypigeonsample.data.network.Resource
import com.tekkr.flypigeonsample.data.repositories.FlightsSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val flightSearchRepo: FlightsSearchRepo
) : ViewModel() {

    fun getOneWayFlightSearchResults(queryParams: HashMap<String, String>): LiveData<Resource<OneWayFlightsSearchResult>> {
        val response: MutableLiveData<Resource<OneWayFlightsSearchResult>> = MutableLiveData()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response.postValue(Resource.Loading)
                response.postValue(flightSearchRepo.getOneWayFlightsSearchResults(queryParams))
            }
        }

        return response
    }

    fun getRoundTripFlightSearchResults(queryParams: HashMap<String, String>): LiveData<Resource<RoundTripFlightSearchResult>> {
        val response: MutableLiveData<Resource<RoundTripFlightSearchResult>> = MutableLiveData()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response.postValue(Resource.Loading)
                response.postValue(flightSearchRepo.getRoundTripFlightsSearchResults(queryParams))
            }
        }

        return response
    }

}