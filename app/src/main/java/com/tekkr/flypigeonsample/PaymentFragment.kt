package com.tekkr.flypigeonsample

import android.app.Activity
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
                    retryObj.put("enabled", true)
                    put("retry", retryObj)

                }
                co.open(activity, options)
            } catch (e: Exception) {
                handlePaymentFailedCase()
                e.printStackTrace()
            }
        }


    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        Log.e("paymentData", paymentData.toString())
    }

    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {

    }

    private fun handlePaymentFailedCase() {
        tv_payment_status.text = "Payment Failed Please Try agian.."
        with(btn_payment) {
            text = "Try Again"
            setOnClickListener {
                startPaymentProcess()
            }
        }
    }

}