package com.tekkr.flypigeonsample.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.marginStart
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.tekkr.flypigeonsample.*
import kotlinx.android.synthetic.main.fragment_one_way.*
import kotlinx.android.synthetic.main.fragment_one_way.rl_class
import kotlinx.android.synthetic.main.fragment_one_way.rl_dep_date_picker
import kotlinx.android.synthetic.main.fragment_one_way.rl_from
import kotlinx.android.synthetic.main.fragment_one_way.rl_to
import kotlinx.android.synthetic.main.fragment_one_way.rl_travellers
import kotlinx.android.synthetic.main.fragment_one_way.tv_dep_date
import kotlinx.android.synthetic.main.fragment_round_trip.*
import kotlinx.android.synthetic.main.travellers_count_toggle_btn.view.*
import kotlinx.android.synthetic.main.travellers_selection_bottom_sheet.*
import kotlinx.android.synthetic.main.travellers_selection_item.*
import kotlinx.android.synthetic.main.travellers_selection_item.view.*
import java.util.*


class OneWayFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one_way, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rl_from.setOnClickListener {
            launchSearchAirportsActivity(Constants.origin){ r1, r2 ->
                tv_from_airport_title.text = r1
                tv_from_airport_desc.text= r2
            }
        }
        rl_to.setOnClickListener {
            launchSearchAirportsActivity(Constants.destination){r1, r2 ->
                tv_to_airport_title.text = r1
                tv_to_airport_desc.text= r2
            }
        }

        //create instance of calendar for bounds
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val currentDate = calendar.timeInMillis


        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        val jan = calendar.timeInMillis

        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        val dec = calendar.timeInMillis


        val calConstraintsBuilder = CalendarConstraints.Builder()
        calConstraintsBuilder.apply {
            setStart(jan)
            setEnd(dec)
            setValidator(DateValidatorPointForward.now())
        }

        //init and launch date picker
        val depDatePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select a Departure Date")
                .setCalendarConstraints(calConstraintsBuilder.build())
                .setSelection(currentDate)
                .build()

        rl_dep_date_picker.setOnClickListener {

            depDatePicker.show(parentFragmentManager, "DEP_DATE_PICKER")

        }

        depDatePicker.addOnPositiveButtonClickListener {
            tv_dep_date.text = depDatePicker.headerText
        }

        rl_travellers.setOnClickListener {
            showTravellersSelectionBottomSheet()
        }

        rl_class.setOnClickListener {
            showClassPopupMenu()
        }


    }



}