package com.tekkr.flypigeonsample.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.activity.result.contract.ActivityResultContracts
import com.tekkr.flypigeonsample.CalendarPickerActivity
import com.tekkr.flypigeonsample.R
import kotlinx.android.synthetic.main.fragment_one_way.*
import com.tekkr.flypigeonsample.SearchAirportsActivity
import com.tekkr.flypigeonsample.showToast
import kotlinx.android.synthetic.main.fragment_one_way.*


class OneWayFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one_way, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //invoke date picker dialog
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Departure Date").build()
        datePicker.addOnPositiveButtonClickListener {
            tv_dep_date.text = datePicker.headerText
        }

        rl_dep_date_picker.setOnClickListener {
            datePicker.show(parentFragmentManager,"DEP_DATE_PICKER")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rl_from.setOnClickListener {
            launchSearchAirportsActivity("Origin")
        }
        rl_to.setOnClickListener {
            launchSearchAirportsActivity("Destination")
        }
        rl_dep_date_picker.setOnClickListener {
            launchDatePicker()
        }
    }

    private fun launchSearchAirportsActivity(source_type: String) {
        airportSearchActvLauncher.launch(
            Intent(
                requireContext(),
                SearchAirportsActivity::class.java
            ).apply {
                putExtra("source_type", source_type)
            }
        )
    }

    private fun launchDatePicker() {
        datePickerLauncher.launch(
            Intent(requireContext(), CalendarPickerActivity::class.java)
        )
    }

    private val airportSearchActvLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    tv_from_airport_title.text = it.getStringExtra("source_city_code_name")
                    tv_from_airport_desc.text = it.getStringExtra("source_city_full_name")
                    tv_to_airport_title.text = it.getStringExtra("dest_city_code_name")
                    tv_to_airport_desc.text = it.getStringExtra("dest_city_full_name")
                }
            }
        }

    private val datePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
