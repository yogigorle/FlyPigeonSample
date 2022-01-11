package com.tekkr.flypigeonsample.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.network.Resource
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.DataStoreManager
import com.tekkr.flypigeonsample.utils.ProgressBarHandler
import com.tekkr.flypigeonsample.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.progress_bar_layout.*
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    protected val baseViewModel: BaseViewModel by viewModels()

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
//        val progressBarHandler = ProgressBarHandler(this)
        response?.let {
            when (it) {
//                is Resource.Loading -> progressBarHandler.show()
                is Resource.Success -> {
//                    progressBarHandler.hide()
                    onSuccess(it.value)
                }
                is Resource.Failure -> {
//                    progressBarHandler.hide()
                    handleApiError(it) { onRetry?.invoke() }
                }

            }
        }
    }





}