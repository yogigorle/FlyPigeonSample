package com.tekkr.flypigeonsample.utils

import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.tekkr.flypigeonsample.R
import kotlinx.android.synthetic.main.progress_bar_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressBarHandler(
    private val activity: Activity
) {

    private lateinit var view: View
    val layout = activity.findViewById<View>(android.R.id.content).rootView as ViewGroup

    private val uiScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    fun show() {

        uiScope.launch {
            try {

                activity.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

                view = activity.layoutInflater.inflate(R.layout.progress_bar_layout, null)
                layout.addView(view)
                view.progress_bar_view.visibility = View.VISIBLE

                delay(10000)
                hide()

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hide() {
        uiScope.launch {
            try {
                activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                if (::view.isInitialized)
                    layout.removeView(view)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

    }
}
