package com.tekkr.flypigeonsample.ui.views.flights

import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirFareItinerary
import com.tekkr.flypigeonsample.databinding.OneWayFlightInfoItemBinding
import com.tekkr.flypigeonsample.databinding.RoundTripFlightInfoItemBinding
import com.tekkr.flypigeonsample.utils.BaseBindingAdapter
import com.tekkr.flypigeonsample.utils.diffChecker

class RoundTripFlightsListAdapter :
    BaseBindingAdapter<AirFareItinerary, RoundTripFlightInfoItemBinding>(
        R.layout.round_trip_flight_info_item,
        diffChecker { old, new -> old.fareItinerary.airItineraryFareInfo.FareSourceCode == new.fareItinerary.airItineraryFareInfo.FareSourceCode }
    ) {
    override fun onBindViewHolder(holder: Holder<RoundTripFlightInfoItemBinding>, position: Int) {
        val itemAtPos = getItemAt(position)
        with(holder) {
            itemBinding.apply {
                val originDestinationOption = itemAtPos.fareItinerary.OriginDestinationOptions[0]
                roundTripFlightInfo = originDestinationOption.originDestinationOption[0]
                tvFlightFare.text =
                    itemAtPos.fareItinerary.airItineraryFareInfo.fareBreakdown[0].passengerFare.totalFare.formattedTotalFare
                tvStopClassification.text =
                    if (originDestinationOption.TotalStops > 0) "${originDestinationOption.TotalStops} stop" else "non-stop"
            }

        }
    }

}