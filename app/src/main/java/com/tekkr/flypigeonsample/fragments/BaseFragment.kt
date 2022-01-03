package com.tekkr.flypigeonsample.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tekkr.flypigeonsample.Constants
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.SearchAirportsActivity
import com.tekkr.flypigeonsample.showToast
import kotlinx.android.synthetic.main.fragment_one_way.*
import kotlinx.android.synthetic.main.travellers_count_toggle_btn.view.*
import kotlinx.android.synthetic.main.travellers_selection_bottom_sheet.*
import kotlinx.android.synthetic.main.travellers_selection_item.view.*

abstract class BaseFragment : Fragment() {

    protected fun showTravellersSelectionBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        with(bottomSheet) {
            setContentView(R.layout.travellers_selection_bottom_sheet)

            //create travellers type list
            val travellersTypes = listOf(
                "Adults (12+ yrs)",
                "Children (2-12 yrs)",
                "Infant (0-2 yrs)"
            )

            //start and end index to draw travellers count selector buttons
            var startIndex = 0
            var endIndex = 9

            var travellerCountToggleButton: ToggleButton? = null


            //add views dynamically
            ll_travellers_type_count_placeholder.removeAllViews()
            for (i in travellersTypes.withIndex()) {
                val travellersCountLayout = LayoutInflater.from(requireContext())
                    .inflate(R.layout.travellers_selection_item, null, false)
                with(travellersCountLayout) {
                    tv_traveller_type.text = i.value

                    when (i.index) {
                        0 -> {
                            startIndex = 1
                        }
                        1 -> {
                            startIndex = 0
                            endIndex = 8
                        }
                        else -> {
                            startIndex = 0
                            endIndex = 4
                        }
                    }

                    ll_travellers_count_selector.removeAllViews()
                    for (travellerCount in startIndex..endIndex) {
                        val travellerToggleBtn = LayoutInflater.from(requireContext())
                            .inflate(R.layout.travellers_count_toggle_btn, null, false)
                        travellerCountToggleButton = travellerToggleBtn.travellers_count_toggle_btn
                        travellerCountToggleButton?.text =
                            travellerCount.toString()
                        ll_travellers_count_selector.addView(travellerToggleBtn)

                    }

                    ll_travellers_type_count_placeholder.addView(travellersCountLayout)
                }
            }

            setCancelable(false)
            show()

            btn_done.setOnClickListener {
                hide()
                requireContext().showToast(travellerCountToggleButton?.text.toString())
            }
        }

    }

    protected fun showClassPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), tv_class_title)
        with(popupMenu) {
            menuInflater.inflate(R.menu.flight_seating_class_menu, menu)
            setOnMenuItemClickListener { item ->
                tv_class.text = when (item.itemId) {
                    R.id.economy -> item.title
                    R.id.premium_economy -> item.title
                    R.id.business -> item.title
                    else -> ""
                }
                true
            }
            show()
        }

    }

    protected fun launchSearchAirportsActivity(
        source_type: String,
        result: (String, String) -> Unit
    ) {

        val airportSearchActvLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    data?.let {
                        if (source_type == Constants.origin) {
                            result(
                                it.getStringExtra("source_city_code_name").toString(),
                                it.getStringExtra("source_city_full_name").toString()
                            )

                        }else{
                            result(
                                it.getStringExtra("dest_city_code_name").toString(),
                                it.getStringExtra("dest_city_full_name").toString()
                            )
                        }
                    }
                }
            }

        airportSearchActvLauncher.launch(
            Intent(
                requireContext(),
                SearchAirportsActivity::class.java
            ).apply {
                putExtra("source_type", source_type)

            }
        )


    }


}