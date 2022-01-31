package com.tekkr.flypigeonsample

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.tekkr.flypigeonsample.data.models.BookingDetails
import com.tekkr.flypigeonsample.data.models.ErrorsFromServer
import com.tekkr.flypigeonsample.data.network.Resource
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.ui.viewmodels.FlightBookingViewModel
import com.tekkr.flypigeonsample.ui.views.bookingFlow.TripDetailsShowCaseActivity
import com.tekkr.flypigeonsample.ui.views.bookingFlow.FlightBookingFlowActivity
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.gson
import com.tekkr.flypigeonsample.utils.showToast
import com.tekkr.flypigeonsample.utils.toJson
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.json.JSONObject
import java.lang.Exception


class PaymentFragment : BaseFragment(), FlightBookingFlowActivity.PaymentStatusListener {

    private var flightBookingDetails: BookingDetails? = null
    private var returnFlightBookingDetails: BookingDetails? = null
    private val flightBookingViewModel: FlightBookingViewModel by activityViewModels()
    private var returnFlightPaymentVerification = false
    private var returnFlightBookingStatus = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        flightBookingDetails = flightBookingViewModel.bookingDetails.value
        (activity as FlightBookingFlowActivity?)?.setPaymentStatusListener(this)
        val razorPayOrderId = flightBookingDetails?.razorpay_order_id

        returnFlightBookingDetails = flightBookingViewModel.returnFlightBookingDetails.value

        if (razorPayOrderId != null && flightBookingDetails != null) {
            startPaymentProcess(razorPayOrderId)
        } else {
            requireContext().showToast("Something went wrong please try again..")
        }

        btn_payment.setOnClickListener {

            if (!returnFlightPaymentVerification) {
                startPaymentProcess(returnFlightBookingDetails?.razorpay_order_id)
            } else {
                startPaymentProcess(razorPayOrderId)
            }


        }

    }

    private fun startPaymentProcess(razorpayPaymentId: String?) {
        razorpayPaymentId?.let {
            Checkout.preload(applicationContext)
            val activity: Activity = requireActivity()
            val co = Checkout()
            try {
                val options = JSONObject()
                with(options) {
                    put("name", "Fly Pigeon Test")
                    put("description", "Flight Booking Fee")
                    put("theme.color", "#FF5733")
                    put("order_id", it)
                    put("send_sms_hash", true)
                    put("currency", "INR")

                    val retryObj = JSONObject()
                    retryObj.put("enabled", false)
                    put("retry", retryObj)
                }
                co.open(activity, options)
            } catch (e: Exception) {
                handleSuccessAndFailureCase()
                e.printStackTrace()
            }
        }


    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {

        if (razorpayPaymentId != null && paymentData != null) {
            //verify payment
            flightBookingViewModel.verifyPayment(
                razorpayPaymentId,
                paymentData.orderId,
                paymentData.signature
            ).observe(viewLifecycleOwner, Observer {
                progress_bar_view.visibility = VISIBLE
                handleApiCall(it) { paymentRes ->
                    progress_bar_view.visibility = GONE
                    if (paymentRes.payment_status.equals(
                            "success",
                            true
                        ) && paymentRes.payment_status.equals("success", true)
                    ) {

                        if (returnFlightBookingDetails != null && !returnFlightPaymentVerification) {
                            startPaymentProcess(returnFlightBookingDetails?.razorpay_order_id)
                            returnFlightPaymentVerification = true
                        } else {
                            //handle success case
                            handleSuccessAndFailureCase(true)
                            bookFlight(flightBookingDetails) { oneWayFlightUniqueId ->
                                if (returnFlightBookingDetails != null) {
                                    bookFlight(returnFlightBookingDetails) {
                                        launchTripDetailsActivity(oneWayFlightUniqueId, it)
                                    }
                                } else {
                                    launchTripDetailsActivity(oneWayFlightUniqueId)
                                }
                            }
                        }
                    } else {
                        handleSuccessAndFailureCase(false)
                    }
                }
            })
        } else {
            handleSuccessAndFailureCase(false)
        }


    }

    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {
        Log.e("failurePaymentData", "$errorCode $errorDescription")
        handleSuccessAndFailureCase(false)
        returnFlightPaymentVerification = false
    }

    private fun handleSuccessAndFailureCase(paymentSuccess: Boolean = false) {
        tv_payment_status.text =
            if (paymentSuccess) "Your payment is successful" else "Payment Failed Please Try agian.."
        with(btn_payment) {
            text = if (paymentSuccess) "Paid" else "Try Again"
            if (paymentSuccess) setBackgroundColor(Color.GREEN) else setBackgroundColor(Color.RED)
            if (paymentSuccess) {
                setBackgroundColor(Color.GREEN)
                isEnabled = false
            }
        }
    }

    private fun bookFlight(
        flightBookingDetails: BookingDetails?,
        onBookingSucceeded: (String) -> Unit
    ) {
        flightBookingDetails?.let {
            flightBookingViewModel.bookFlight(it).observe(viewLifecycleOwner, Observer {
                progress_bar_view.visibility = VISIBLE
                tv_booking_status.visibility = VISIBLE
                handleApiCall(it) {
                    progress_bar_view.visibility = GONE
                    val bookingFlightResult = it.flight_data.bookFlightResponse.bookFlightResult
                    if (bookingFlightResult.Success.equals(
                            "true",
                            true
                        )
                    ) {
                        onBookingSucceeded(bookingFlightResult.UniqueID)

                    } else {
                        val errors = gson.fromJson(
                            bookingFlightResult.errors.toString(),
                            ErrorsFromServer::class.java
                        )
                        requireContext().showToast(
                            errors.errors.ErrorMessage
                        )
                        requireActivity().finish()
                    }
                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        requireActivity().finish()
    }

    private fun launchTripDetailsActivity(
        oneWayFlightUniqueId: String,
        roundTripFlightUniqueId: String = ""
    ) {
        tv_booking_status.text =
            "Flight booked successfully, redirecting to ticket details screen."
        //go to ticket dis
        val intent =
            Intent(requireContext(), TripDetailsShowCaseActivity::class.java)
        intent.putExtra(
            Constants.bookingUniqueId,
            oneWayFlightUniqueId
        )
        intent.putExtra(Constants.returnBookingUniqueId, roundTripFlightUniqueId)
        requireActivity().startActivityFromFragment(
            this,
            intent, 100
        )
    }


}