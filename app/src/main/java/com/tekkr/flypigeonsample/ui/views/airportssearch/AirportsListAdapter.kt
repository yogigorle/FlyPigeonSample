package com.tekkr.flypigeonsample.ui.views.airportssearch

import androidx.recyclerview.widget.DiffUtil
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirportsData
import com.tekkr.flypigeonsample.databinding.AirportsInfoItemBinding
import com.tekkr.flypigeonsample.utils.BaseBindingAdapter
import com.tekkr.flypigeonsample.utils.diffChecker

class AirportsListAdapter(val onItemClicked: (AirportsData) -> Unit) :
    BaseBindingAdapter<AirportsData, AirportsInfoItemBinding>(
        R.layout.airports_info_item,
        diffChecker { old, new -> old.id == new.id }
    ) {

    override fun onBindViewHolder(holder: Holder<AirportsInfoItemBinding>, position: Int) {
        val airportData = getItemAt(position)
        with(holder) {
            itemBinding.apply {
                airportInfo = airportData
                cvAirportDataItem.setOnClickListener {
                    onItemClicked(airportData)
                }
            }

        }
    }

}