package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.ui.BaseFragment


class FlightReviewFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_review, container, false)
    }

}