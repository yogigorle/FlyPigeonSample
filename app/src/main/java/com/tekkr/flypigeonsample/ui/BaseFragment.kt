package com.tekkr.flypigeonsample.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.ui.views.airportssearch.SearchAirportsActivity
import kotlinx.android.synthetic.main.fragment_one_way.*
import kotlinx.android.synthetic.main.travellers_count_toggle_btn.view.*
import kotlinx.android.synthetic.main.travellers_selection_bottom_sheet.*
import kotlinx.android.synthetic.main.travellers_selection_item.view.*

abstract class BaseFragment : Fragment() {


    protected fun showTravellersSelectionBottomSheet(
        travellersCount: (Int, Int, Int) -> Unit
    ) {
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


            //declare an empty array to hold toggle buttons
            val adultTravellerToggleButtons = arrayListOf<ToggleButton>()
            val childTravellerToggleButtons = arrayListOf<ToggleButton>()
            val infantTravellerToggleButtons = arrayListOf<ToggleButton>()


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
                        val travellerCountLayout = LayoutInflater.from(requireContext())
                            .inflate(R.layout.travellers_count_toggle_btn, null, false)

                        var selectedToggleBtnList = arrayListOf<ToggleButton>()

                        when (i.index) {
                            0 -> {
                                adultTravellerToggleButtons.add(travellerCountLayout.travellers_count_toggle_btn)

                                adultTravellerToggleButtons[travellerCount - 1].text =
                                    travellerCount.toString()

                                selectedToggleBtnList = adultTravellerToggleButtons
                            }

                            1 -> {
                                childTravellerToggleButtons.add(travellerCountLayout.travellers_count_toggle_btn)

                                childTravellerToggleButtons[travellerCount].text =
                                    travellerCount.toString()

                                selectedToggleBtnList = childTravellerToggleButtons

                            }

                            else -> {
                                infantTravellerToggleButtons.add(travellerCountLayout.travellers_count_toggle_btn)

                                infantTravellerToggleButtons[travellerCount].text =
                                    travellerCount.toString()
                                selectedToggleBtnList = infantTravellerToggleButtons

                            }
                        }

                        selectedToggleBtnList.forEach {
                            it.setOnCheckedChangeListener { v, isChecked ->
                                if (isChecked) {
                                    selectedToggleBtnList.filter { it != v && it.isChecked }
                                        .forEach {
                                            it.isChecked = false
                                        }
                                }
                            }
                        }

                        ll_travellers_count_selector.addView(travellerCountLayout)

                        btn_done.setOnClickListener {

                            adultTravellerToggleButtons.forEach { adults ->
                                childTravellerToggleButtons.forEach { children ->
                                    infantTravellerToggleButtons.forEach { infants ->
                                        if (adults.isChecked && children.isChecked && infants.isChecked) {
                                            travellersCount(
                                                adults.text.toString().toInt(),
                                                children.text.toString().toInt(),
                                                infants.text.toString().toInt()
                                            )
                                        }
                                    }
                                }
                            }
                            hide()

                        }
                    }

                    ll_travellers_type_count_placeholder.addView(travellersCountLayout)
                }
            }
            setCancelable(false)
            show()


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
        airportSearchActvLauncher: ActivityResultLauncher<Intent>
    ) {
        airportSearchActvLauncher.launch(
            Intent(
                requireContext(),
                SearchAirportsActivity::class.java
            ).apply {
                putExtra("source_type", source_type)
            }
        )

    }

    protected fun interChangeSrcAndDest(
        imageView: ImageView,
        srcAirportCodeTextView: TextView,
        srcCityTextView: TextView,
        destAirportCodeTextView: TextView,
        destCityTextView: TextView,

        ) {

        //start rotating animation
        ObjectAnimator.ofFloat(imageView, View.ROTATION, -360f, 0f).apply {
            duration = 400
            disableViewDuringAnimation(imageView)
            start()
        }

        val srcAirportCode = srcAirportCodeTextView.text
        val srcCity = srcCityTextView.text
        //now interchange the source and destinations
        srcAirportCodeTextView.text = destAirportCodeTextView.text
        srcCityTextView.text = destCityTextView.text
        destAirportCodeTextView.text = srcAirportCode
        destCityTextView.text = srcCity
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        this.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

}