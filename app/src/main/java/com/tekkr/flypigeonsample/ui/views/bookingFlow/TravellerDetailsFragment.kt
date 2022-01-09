package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.TravellerDetails
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.checkIfStringsEmpty
import com.tekkr.flypigeonsample.utils.showToast
import kotlinx.android.synthetic.main.add_travellers_info_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_flight_review.tv_title
import kotlinx.android.synthetic.main.fragment_traveller_details.*

class TravellerDetailsFragment : BaseFragment() {

    private val adultTravellerDetailsAdapter by lazy { TravellerDetailsAdapter() }
    private val childTravellerDetailsAdapter by lazy { TravellerDetailsAdapter() }
    private val infantTravellerDetailsAdapter by lazy { TravellerDetailsAdapter() }
    val adultsTravellersList = arrayListOf<TravellerDetails>()
    val childTravellersList = arrayListOf<TravellerDetails>()
    val infantsTravellersList = arrayListOf<TravellerDetails>()
    var adultsCount = 0
    var childrenCount = 0
    var infantsCount = 0

    private val args: TravellerDetailsFragmentArgs by navArgs()
    var isPassportMandatory = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traveller_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(args) {
            tv_src_and_dest.text = srcAndDestCity
            tv_journey_overview_traveller_details.text = flightInfo
            tv_adults_count.text = adultTravellers.toString()
            tv_children_count.text = childTravellers.toString()
            tv_infants_count.text = infantTravellers.toString()
            isPassportMandatory = isPassportRequired
        }

        btn_add_adult.setOnClickListener {
            invokeTravellerDetailsBottomSheet(Constants.TravellerType.Adult.type) {
                adultsTravellersList.add(it)
                adultTravellerDetailsAdapter.submitList(adultsTravellersList)
                rv_adult_travellers.adapter = adultTravellerDetailsAdapter
            }
        }
        btn_add_children.setOnClickListener {
            invokeTravellerDetailsBottomSheet(Constants.TravellerType.Child.type) {
                childTravellersList.add(it)
                childTravellerDetailsAdapter.submitList(childTravellersList)
                rv_child_travellers.adapter = childTravellerDetailsAdapter
            }
        }
        btn_add_infant.setOnClickListener {
            invokeTravellerDetailsBottomSheet(Constants.TravellerType.Infant.type) {
                infantsTravellersList.add(it)
                infantTravellerDetailsAdapter.submitList(infantsTravellersList)
                rv_infant_travellers.adapter = infantTravellerDetailsAdapter
            }

        }
    }

    private fun invokeTravellerDetailsBottomSheet(
        travellerType: String,
        result: (TravellerDetails) -> Unit
    ) {

        var selectedGender = ""

        val bottomSheet = BottomSheetDialog(requireContext())
        with(bottomSheet) {
            setContentView(R.layout.add_travellers_info_bottom_sheet)
            if (isPassportMandatory) {
                tl_passport_number.visibility = VISIBLE
                tl_passport_expiry_date.visibility = VISIBLE
                tl_passport_issue_date.visibility = VISIBLE
            }
            when (travellerType) {
                Constants.TravellerType.Adult.type -> {
                    tv_title.text = "Add Adult Details"
                }
                Constants.TravellerType.Child.type -> {
                    tv_title.text = "Add Child Details"
                    gender_first_btn.text = "Miss."
                    gender_sec_btn.text = "Master."
                    gender_third_btn.visibility = GONE
                }

                Constants.TravellerType.Infant.type -> {
                    tv_title.text = "Add Infant Details"
                    gender_first_btn.text = "Miss."
                    gender_sec_btn.text = "Master."
                    gender_third_btn.visibility = GONE
                }
            }

            toggle_button_group.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    selectedGender = when (checkedId) {
                        R.id.gender_first_btn -> {
                            "Male"

                        }
                        R.id.gender_sec_btn -> "Female"
                        else -> "Female"
                    }
                }
            }

            et_user_dob.setOnClickListener {
                invokeDatePicker(parentFragmentManager) {
                    with(et_user_dob) {
                        setText(it)
                    }
                }
            }

            et_passport_issue_date.setOnClickListener {
                invokeDatePicker(parentFragmentManager) {
                    with(et_user_dob) {
                        setText(it)
                    }
                }
            }

            et_passport_expiry_date.setOnClickListener {
                invokeDatePicker(parentFragmentManager) {
                    with(et_user_dob) {
                        setText(it)
                    }
                }
            }

            btn_save_traveller.setOnClickListener {
                val firstName = et_user_first_name.text.toString()
                val lastName = et_user_last_name.text.toString()
                val dob = et_user_dob.text.toString()
                val passportNo = et_passport.text.toString()
                val passportIssueDate = et_passport_issue_date.text.toString()
                val passportExpiryDate = et_passport_expiry_date.text.toString()
                checkIfStringsEmpty(
                    listOf(selectedGender, firstName, lastName, dob)
                ) {
                    if (it) {
                        requireContext().showToast("All Fields are Mandatory...")
                    } else {
                        if (isPassportMandatory) {
                            checkIfStringsEmpty(
                                listOf(
                                    passportNo,
                                    passportIssueDate,
                                    passportExpiryDate
                                )
                            ) {
                                if (it) {
                                    requireContext().showToast("All Fields along with Passport details are Mandatory")
                                } else {

                                    val id = when (travellerType) {
                                        Constants.TravellerType.Adult.type -> {
                                            adultsCount++
                                        }
                                        Constants.TravellerType.Child.type -> {
                                            childrenCount++
                                        }
                                        Constants.TravellerType.Infant.type -> {
                                            infantsCount++
                                        }
                                        else -> 0
                                    }


                                    result(
                                        TravellerDetails(
                                            id,
                                            firstName,
                                            lastName,
                                            dob,
                                            passportNo,
                                            passportIssueDate,
                                            passportExpiryDate,
                                            selectedGender
                                        )
                                    )
                                    dismiss()
                                }
                            }

                        } else {

                            val id = when (travellerType) {
                                Constants.TravellerType.Adult.type -> {
                                    adultsCount++
                                }
                                Constants.TravellerType.Child.type -> {
                                    childrenCount++
                                }
                                Constants.TravellerType.Infant.type -> {
                                    infantsCount++
                                }
                                else -> 0
                            }

                            result(
                                TravellerDetails(
                                    id,
                                    firstName,
                                    lastName,
                                    dob,
                                    gender = selectedGender
                                )
                            )
                            dismiss()

                        }

                    }

                }

            }

            setCancelable(true)
            show()
        }
    }

    private fun invokeDatePicker(
        fragmentManager: FragmentManager,
        result: (String) -> Unit
    ) {

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Desired Date")
            .build()

        with(datePicker) {
            show(fragmentManager, "DATE_PICKER")
            addOnPositiveButtonClickListener {
                it?.let {
                    result(headerText)
                }
            }
        }
    }

}