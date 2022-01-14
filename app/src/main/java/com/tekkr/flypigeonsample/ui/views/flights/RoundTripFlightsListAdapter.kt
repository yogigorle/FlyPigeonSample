package com.tekkr.flypigeonsample.ui.views.flights

import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirFareItinerary
import com.tekkr.flypigeonsample.data.models.RoundTripAirFareItinerary
import com.tekkr.flypigeonsample.data.models.RoundTripF
import com.tekkr.flypigeonsample.databinding.OneWayFlightInfoItemBinding
import com.tekkr.flypigeonsample.databinding.RoundTripFlightInfoItemBinding
import com.tekkr.flypigeonsample.utils.BaseBindingAdapter
import com.tekkr.flypigeonsample.utils.diffChecker


class RoundTripFlightsListAdapter(val flightType: String, val onFlightSelected: (RoundTripAirFareItinerary, String) -> Unit) :
    BaseBindingAdapter<RoundTripAirFareItinerary, RoundTripFlightInfoItemBinding>(
        R.layout.round_trip_flight_info_item,
        diffChecker { old, new -> old.fareItinerary.airItineraryFareInfo.FareSourceCode == new.fareItinerary.airItineraryFareInfo.FareSourceCode }
    ) {

    private var selectedItemIndex = -1
    private var lastSelectedItemIndex = -1

    override fun onBindViewHolder(holder: Holder<RoundTripFlightInfoItemBinding>, position: Int) {
        val itemAtPos = getItemAt(position)
        val originDestinationOption = itemAtPos.OriginDestinationOptions[0]
        originDestinationOption.isFlightSelected = selectedItemIndex == position
        with(holder) {
            itemBinding.apply {
                roundTripFlightInfo = originDestinationOption
                tvFlightFare.text =
                    itemAtPos.fareItinerary.airItineraryFareInfo.fareBreakdown.passengerFare.totalFare.formattedTotalFare

                clRoundTripFlight.setOnClickListener {
                    selectedItemIndex = adapterPosition
                    //we are calling this to deselect at last selected position
                    notifyItemChanged(lastSelectedItemIndex)
                    lastSelectedItemIndex = selectedItemIndex
                    //we are calling this to deselect at selected position
                    notifyItemChanged(selectedItemIndex)
                    onFlightSelected(itemAtPos,flightType)
                }


            }


        }
    }

}