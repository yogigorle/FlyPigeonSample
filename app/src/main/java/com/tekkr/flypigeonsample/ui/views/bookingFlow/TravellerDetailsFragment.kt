package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.BookingDetails
import com.tekkr.flypigeonsample.data.models.TravellerDetails
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.ui.viewmodels.FlightBookingViewModel
import com.tekkr.flypigeonsample.ui.viewmodels.FlightSearchViewModel
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
    private val adultsTravellersList = arrayListOf<TravellerDetails>()
    private val childTravellersList = arrayListOf<TravellerDetails>()
    private val infantsTravellersList = arrayListOf<TravellerDetails>()
    private var addedAdultsCount = 0
    private var addedChildCount = 0
    private var addedInfantsCount = 0
    private var totalAdultsCount = 0
    private var totalChildrenCount = 0
    private var totalInfantsCount = 0

    private val args: TravellerDetailsFragmentArgs by navArgs()
    private var isPassportMandatory = false

    private val flightBookingViewModel: FlightBookingViewModel by activityViewModels()

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
            totalAdultsCount = adultTravellers
            totalChildrenCount = childTravellers
            totalInfantsCount = infantTravellers
            tv_adults_count.text = totalAdultsCount.toString()
            tv_children_count.text = totalChildrenCount.toString()
            tv_infants_count.text = totalInfantsCount.toString()
            isPassportMandatory = isPassportRequired
        }

        btn_add_adult.setOnClickListener {
            setAdapter(
                totalAdultsCount,
                Constants.TravellerType.Adult.type,
                adultsTravellersList,
                adultTravellerDetailsAdapter
            )
        }
        btn_add_children.setOnClickListener {
            setAdapter(
                totalChildrenCount,
                Constants.TravellerType.Child.type,
                childTravellersList,
                childTravellerDetailsAdapter
            )
        }
        btn_add_infant.setOnClickListener {
            setAdapter(
                totalInfantsCount,
                Constants.TravellerType.Infant.type,
                infantsTravellersList,
                infantTravellerDetailsAdapter
            )

        }
    }

    private fun invokeTravellerDetailsBottomSheet(
        travellerType: String,
        result: (TravellerDetails) -> Unit
    ) {

        var selectedGender = ""
        var dobInMilliseconds = 0L
        var passportExpiryDateInMillis = 0L


        val bottomSheet = BottomSheetDialog(requireContext())
        with(bottomSheet) {
            setContentView(R.layout.add_travellers_info_bottom_sheet)
            if (isPassportMandatory) {
                tl_passport_number.visibility = VISIBLE
                tl_passport_expiry_date.visibility = VISIBLE
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

            toggle_button_group.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    selectedGender = when (checkedId) {
                        R.id.gender_first_btn -> {
                            Constants.Gender.Male.shortName

                        }
                        R.id.gender_sec_btn -> Constants.Gender.Female.shortName
                        else -> Constants.Gender.Female.shortName
                    }
                }
            }

            et_user_dob.setOnClickListener {
                invokeDatePicker(parentFragmentManager) { dobText, dobInMillis ->
                    dobInMilliseconds = dobInMillis
                    with(et_user_dob) {
                        setText(dobText)
                    }
                }
            }

            et_passport_expiry_date.setOnClickListener {
                invokeDatePicker(parentFragmentManager) { passportExpiryDate, passportExpiryDateMillis ->
                    passportExpiryDateInMillis = passportExpiryDateMillis
                    with(et_passport_expiry_date) {
                        setText(passportExpiryDate)
                    }
                }
            }

            btn_save_traveller.setOnClickListener {
                val firstName = et_user_first_name.text.toString()
                val lastName = et_user_last_name.text.toString()
                val dob = et_user_dob.text.toString()
                val passportNo = et_passport.text.toString()
                val passportExpiryDate = et_passport_expiry_date.text.toString()
                val travellerTitle =
                    if (selectedGender == Constants.Gender.Male.name) "Mr" else "Ms"

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
                                    passportExpiryDate
                                )
                            ) {
                                if (it) {
                                    requireContext().showToast("All Fields along with Passport details are Mandatory")
                                } else {
                                    addTravellerDetails(
                                        travellerType,
                                        firstName,
                                        lastName,
                                        dobInMilliseconds,
                                        passportNo,
                                        passportExpiryDateInMillis,
                                        selectedGender,
                                        travellerTitle
                                    ) {
                                        result(it)
                                    }
                                    dismiss()
                                }
                            }

                        } else {
                            addTravellerDetails(
                                travellerType,
                                firstName,
                                lastName,
                                dobInMilliseconds,
                                passportNo,
                                passportExpiryDateInMillis,
                                selectedGender,
                                travellerTitle
                            ) {
                                result(it)
                            }
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
        result: (String, Long) -> Unit
    ) {

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Desired Date")
            .build()

        with(datePicker) {
            show(fragmentManager, "DATE_PICKER")
            addOnPositiveButtonClickListener {
                it?.let {
                    result(headerText, it)
                }
            }
        }
    }

    private fun setAdapter(
        travellersCount: Int,
        travellerType: String,
        travellersList: ArrayList<TravellerDetails>,
        adapter: TravellerDetailsAdapter
    ) {
        if (travellersCount == travellersList.size) {
            requireContext().showToast("You can add only $travellersCount $travellerType")
        } else {
            invokeTravellerDetailsBottomSheet(travellerType) {
                travellersList.add(it)
                adapter.submitList(travellersList)
                rv_adult_travellers.adapter = adapter
            }
        }

    }

    private fun addTravellerDetails(
        travellerType: String,
        firstName: String,
        lastName: String,
        dobInMillis: Long = 0L,
        passportNo: String = "",
        passportExpiryDateInMillis: Long = 0L,
        selectedGender: String,
        title: String,
        response: (TravellerDetails) -> Unit
    ) {
        val id = when (travellerType) {
            Constants.TravellerType.Adult.type -> {
                addedAdultsCount++
            }
            Constants.TravellerType.Child.type -> {
                addedChildCount++
            }
            Constants.TravellerType.Infant.type -> {
                addedInfantsCount++
            }
            else -> 0
        }

        response(
            TravellerDetails(
                id,
                travellerType,
                firstName,
                lastName,
                dobInMillis,
                passportNo,
                passportExpiryDateInMillis,
                selectedGender,
                title
            )
        )
    }


    fun navigateToPaymentScreen(razorPayId: String, fareSourceCode: String, onDone: () -> Unit) {

        val userEmail = et_user_email.text.toString()
        val phoneNum = et_user_phn.text.toString()
        val countryCode = et_user_country_code.text.toString()
        val pinCode = et_pin_code.text.toString()

        if (totalAdultsCount == addedAdultsCount && totalChildrenCount == addedChildCount && totalInfantsCount == addedInfantsCount) {
            checkIfStringsEmpty(listOf(userEmail, phoneNum, countryCode, pinCode)) {
                if (it) {
                    requireContext().showToast("All fields are mandatory...")
                } else {
                    val travellerDetailsList = ArrayList<JsonObject>()

                    when (true) {
                        adultsTravellersList.size > 0 -> {
                            for (adultDetails in adultsTravellersList) {
                                travellerDetailsList.add(
                                    with(adultDetails) {
                                        val commonAdultDetails = jsonObject(
                                            "passenger_type" to "ADULT",
                                            "title" to title,
                                            "first_name" to firstName,
                                            "last_name" to lastName,
                                            "dob" to formattedDob,
                                            "frequentFlyrNum" to "",
                                            "mealplan" to "",
                                            "gender" to gender
                                        )

                                        if (isPassportMandatory) {
                                            commonAdultDetails.apply {
                                                addProperty("passport_no", passportNumber)
                                                addProperty(
                                                    "passport_expiry",
                                                    formattedPassportExpiryDate
                                                )
                                                addProperty("issue_country", "India")
                                            }
                                        } else {
                                            commonAdultDetails
                                        }

                                    }
                                )
                            }
                        }
                        childTravellersList.size > 0 -> {
                            for (childTravellers in childTravellersList) {
                                travellerDetailsList.add(
                                    with(childTravellers) {
                                        val commonChildDetails = jsonObject(
                                            "passenger_type" to "CHILD",
                                            "title" to title,
                                            "first_name" to firstName,
                                            "last_name" to lastName,
                                            "dob" to formattedDob,
                                            "frequentFlyrNum" to "",
                                            "mealplan" to "",
                                            "gender" to gender
                                        )

                                        if (isPassportMandatory) {
                                            commonChildDetails.apply {
                                                addProperty("passport_no", passportNumber)
                                                addProperty(
                                                    "passport_expiry",
                                                    formattedPassportExpiryDate
                                                )
                                            }
                                        } else {
                                            commonChildDetails
                                        }
                                    }
                                )
                            }
                        }

                        infantsTravellersList.size > 0 -> {
                            for (infantTravellers in infantsTravellersList) {
                                travellerDetailsList.add(
                                    with(infantTravellers) {
                                        jsonObject(
                                            "passenger_type" to "INFANT",
                                            "title" to title,
                                            "first_name" to firstName,
                                            "last_name" to lastName,
                                            "dob" to formattedDob,
                                            "mealplan" to "",
                                            "gender" to gender
                                        )
                                    }
                                )
                            }
                        }
                    }

                    val bookingDetails = BookingDetails(
                        fareSourceCode,
                        isPassportMandatory.toString(),
                        pinCode,
                        totalAdultsCount,
                        "415",
                        travellerDetailsList,
                        "WebFare",
                        totalChildrenCount,
                        countryCode,
                        userEmail,
                        totalInfantsCount,
                        phoneNum,
                        razorPayId
                    )

                    flightBookingViewModel.bookingDetails.value = bookingDetails


                    val action =
                        TravellerDetailsFragmentDirections.actionTravellerDetailsFragmentToPaymentFragment(
                        )
                    navigateByAction(action)
                    onDone.invoke()
                }
            }
        } else {
            requireContext().showToast("Add Traveller Details.")
        }

    }


}