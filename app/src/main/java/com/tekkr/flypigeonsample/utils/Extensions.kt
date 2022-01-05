package com.tekkr.flypigeonsample.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun checkIfStringsEmpty(
    stringsList: List<String>,
    result: (Boolean) -> Unit
) {
    var isStringEmpty = false

    stringsList.forEach {
        isStringEmpty = it.isEmpty()
    }

    result(isStringEmpty)

}

fun Long.convertMillsToDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(this)
}

val gson = Gson()
fun Any.toJson() = gson.toJson(this)

fun <T> diffChecker(itemsSame: (T, T) -> Boolean): DiffUtil.ItemCallback<T> where T : Any {
    return object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return itemsSame(oldItem, newItem)

        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return (oldItem.toJson() == newItem.toJson())
        }

    }
}

fun Any.formatMoney(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "in"))
    numberFormat.maximumFractionDigits = 0

    return numberFormat.format(this)
}

fun String.formatFlightTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.time = dateFormat.parse(this)!!
    val hoursAndMins = Formatter().format("%tl:%tM",calendar,calendar)
    return hoursAndMins.toString()
}

fun Int.convertToHoursAndMins(): String {
    val hours = this / 60
    val mins = this % 60

    return "${hours}h ${mins}m"

}