package com.tekkr.flypigeonsample.ui.views.flights

import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.FlightsSearchResult
import com.tekkr.flypigeonsample.databinding.OneWayFlightInfoItemBinding
import com.tekkr.flypigeonsample.utils.BaseBindingAdapter
import com.tekkr.flypigeonsample.utils.diffChecker

class FlightsListAdapter :
    BaseBindingAdapter<FlightsSearchResult.FlightsSearchResponse.FlightSearchInfo.AirFareItinerary, OneWayFlightInfoItemBinding>(
        R.layout.one_way_flight_info_item,
        diffChecker { old, new -> old.fareItinerary.airItineraryFareInfo.FareSourceCode == new.fareItinerary.airItineraryFareInfo.FareSourceCode }
    ) {
    override fun onBindViewHolder(holder: Holder<OneWayFlightInfoItemBinding>, position: Int) {
        val itemAtPos = getItemAt(position)
        with(holder) {
            itemBinding.apply {
                val originDestinationOption = itemAtPos.fareItinerary.OriginDestinationOptions[0]
                flightInfo = originDestinationOption.originDestinationOption[0]
                tvFlightFare.text =
                    itemAtPos.fareItinerary.airItineraryFareInfo.fareBreakdown[0].passengerFare.totalFare.formattedTotalFare
                tvStopClassification.text =
                    if (originDestinationOption.TotalStops > 0) "${originDestinationOption.TotalStops} stop" else "non-stop"
            }

        }
    }

}