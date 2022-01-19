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
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.ui.viewmodels.FlightBookingViewModel
import com.tekkr.flypigeonsample.ui.views.bookingFlow.TripDetailsShowCaseActivity
import com.tekkr.flypigeonsample.ui.views.bookingFlow.FlightBookingFlowActivity
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.showToast
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.json.JSONObject
import java.lang.Exception


class PaymentFragment : BaseFragment(), FlightBookingFlowActivity.PaymentStatusListener {

    private var razorPayOrderId: String? = null
    private var flightBookingDetails: BookingDetails? = null
    private val flightBookingViewModel: FlightBookingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as FlightBookingFlowActivity?)?.setPaymentStatusListener(this)

        flightBookingDetails = flightBookingViewModel.bookingDetails.value
        razorPayOrderId = flightBookingDetails?.razorpay_order_id

        if (razorPayOrderId != null && flightBookingDetails != null) {
            startPaymentProcess()
        } else {
            requireContext().showToast("Something went wrong please try again..")
        }

        btn_payment.setOnClickListener {
            razorPayOrderId?.let {
                startPaymentProcess()
            }
        }

    }

    private fun startPaymentProcess() {
        razorPayOrderId?.let {
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
                        //handle success case
                        handleSuccessAndFailureCase(true)
                        bookFlight()
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

    private fun bookFlight() {
        flightBookingDetails?.let {
            flightBookingViewModel.bookFlight(it).observe(viewLifecycleOwner, Observer {
                progress_bar_view.visibility = VISIBLE
                tv_booking_status.visibility = VISIBLE
                handleApiCall(it) {
                    progress_bar_view.visibility = GONE
                    with(it.flight_data.bookFlightResponse.bookFlightResult) {
                        if (Success.equals("true", true)) {
                            tv_booking_status.text =
                                "Flight booked successfully, redirecting to ticket details screen."
                            //go to ticket dis
                            val intent =
                                Intent(requireContext(), TripDetailsShowCaseActivity::class.java)
                            intent.putExtra(
                                Constants.bookingUniqueId,
                                this.UniqueID
                            )
                            requireActivity().startActivityFromFragment(
                                this@PaymentFragment,
                                intent,
                                100
                            )
                        }
                    }
                }
            })
        }
    }


}