package com.tekkr.flypigeonsample.ui.views.bookingFlow

import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.TravellerDetails
import com.tekkr.flypigeonsample.databinding.LayoutTravellerDetailsItemBinding
import com.tekkr.flypigeonsample.utils.BaseBindingAdapter
import com.tekkr.flypigeonsample.utils.diffChecker

class TravellerDetailsAdapter :
    BaseBindingAdapter<TravellerDetails, LayoutTravellerDetailsItemBinding>(
        R.layout.layout_traveller_details_item, diffChecker { old, new -> old.id == new.id }
    ) {

    override fun onBindViewHolder(
        holder: Holder<LayoutTravellerDetailsItemBinding>,
        position: Int
    ) {
        val currentTravellerDetails = getItemAt(position)
        with(holder) {
            itemBinding.apply {
                travellerDetails = currentTravellerDetails
            }
        }
    }
}