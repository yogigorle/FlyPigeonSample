package com.tekkr.flypigeonsample.ui

import android.app.Activity
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.network.Resource
import com.tekkr.flypigeonsample.utils.showToast
import kotlinx.android.synthetic.main.progress_bar_layout.*

abstract class BaseActivity : AppCompatActivity() {

    protected fun handleApiError(
        failure: Resource.Failure,
        retry: (() -> Unit)? = null
    ) {
        when {
            failure.isNetworkError -> showToast(
                "Please check your internet connection"
            )
            failure.errorCode == 401 -> {

            }
            else -> {
                val error = failure.errorBody?.string().toString()
                showToast(if (error.isEmpty()) "Sorry Something Went Wrong..." else error)
            }
        }
    }

    protected inline fun <T> handleApiCall(
        response: Resource<T>?,
        noinline onRetry: (() -> Unit)? = null,
        onSuccess: (T) -> Unit
    ) {
        response?.let {
            when (it) {
                is Resource.Success -> onSuccess(it.value)
                is Resource.Failure -> handleApiError(it) { onRetry?.invoke() }

            }
        }
    }

}