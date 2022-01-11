package com.tekkr.flypigeonsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    val srcAirportName: LiveData<String?>
        get() = dataStoreManager.getString(Constants.FlightJourneyParams.SrcAirportName.param)
            .asLiveData()

    val destAirportName: LiveData<String?>
        get() = dataStoreManager.getString(Constants.FlightJourneyParams.DestAirportName.param)
            .asLiveData()

}