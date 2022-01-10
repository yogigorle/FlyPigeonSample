package com.tekkr.flypigeonsample.ui.viewmodels

import androidx.lifecycle.*
import com.tekkr.flypigeonsample.data.models.AirportsData
import com.tekkr.flypigeonsample.data.models.OneWayFlightsSearchResult
import com.tekkr.flypigeonsample.data.models.RevalidateFlightResult
import com.tekkr.flypigeonsample.data.models.RoundTripFlightSearchResult
import com.tekkr.flypigeonsample.data.network.Resource
import com.tekkr.flypigeonsample.data.repositories.FlightsSearchRepo
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val flightSearchRepo: FlightsSearchRepo
) : ViewModel() {

    private val _filteredAirportsData: MutableLiveData<Resource<List<AirportsData>>> =
        MutableLiveData()
    val filteredAirportsData: LiveData<Resource<List<AirportsData>>> = _filteredAirportsData

    fun getAirportsList(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _filteredAirportsData.postValue(Resource.Loading)
            _filteredAirportsData.postValue(flightSearchRepo.getAirportsList(query))

        }
    }

    fun getSrcAirportName(): LiveData<String?> =
        dataStoreManager.getString(Constants.FlightJourneyParams.SrcAirportName.param).asLiveData()

    fun getDestAirportName(): LiveData<String?> =
        dataStoreManager.getString(Constants.FlightJourneyParams.DestAirportName.param).asLiveData()

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

        viewModelScope.launch(Dispatchers.IO) {
            response.postValue(Resource.Loading)
            response.postValue(flightSearchRepo.getRoundTripFlightsSearchResults(queryParams))
        }

        return response
    }

    fun revalidateFlight(fareSourceCode: String): LiveData<Resource<RevalidateFlightResult>> {
        val response: MutableLiveData<Resource<RevalidateFlightResult>> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            response.postValue(Resource.Loading)
            response.postValue(flightSearchRepo.revalidateFlight(fareSourceCode))
        }

        return response
    }

}