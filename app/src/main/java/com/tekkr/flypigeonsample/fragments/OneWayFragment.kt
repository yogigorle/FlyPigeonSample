package com.tekkr.flypigeonsample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.tekkr.flypigeonsample.R
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


}