package com.tekkr.flypigeonsample.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

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