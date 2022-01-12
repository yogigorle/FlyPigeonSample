package com.tekkr.flypigeonsample

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.github.salomonbrys.kotson.put
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.ui.views.bookingFlow.FlightBookingFlowActivity
import com.tekkr.flypigeonsample.utils.toJson
import kotlinx.android.synthetic.main.fragment_payment.*
import org.json.JSONObject
import java.lang.Exception


class PaymentFragment : BaseFragment(), FlightBookingFlowActivity.PaymentStatusListener {

    private val safeArgs: PaymentFragmentArgs by navArgs()
    private var paymentOrderId: String? = null

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

        safeArgs.paymentOrderId?.let {
            paymentOrderId = it
            startPaymentProcess()
        }

        btn_payment.setOnClickListener {
            paymentOrderId?.let {
                startPaymentProcess()
            }
        }

    }

    private fun startPaymentProcess() {
        paymentOrderId?.let {
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
        Log.e("successPaymentData", paymentData?.toJson()!!)
        handleSuccessAndFailureCase(true)
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

}