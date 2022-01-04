package com.tekkr.flypigeonsample.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekkr.flypigeonsample.data.models.FlightsSearchResult
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

    fun getFlightSearchResults(queryParams: HashMap<String, String>): LiveData<Resource<FlightsSearchResult>> {
        val response: MutableLiveData<Resource<FlightsSearchResult>> = MutableLiveData()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response.postValue(flightSearchRepo.getFlightsSearchResults(queryParams))
            }
        }

        return response
    }

}