package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirFareItinerary
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.showToast
import kotlinx.android.synthetic.main.activity_flight_booking_flow.*
import kotlinx.android.synthetic.main.fragment_flight_review.*


class FlightReviewFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_review, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
