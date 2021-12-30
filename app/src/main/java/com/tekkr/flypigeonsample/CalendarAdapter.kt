package com.tekkr.flypigeonsample

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

import android.view.Gravity
import android.view.LayoutInflater


class CalendarAdapter(
    context: Context,
    daysList: ArrayList<Date>
) : ArrayAdapter<Date>(
    context, R.layout.date_item, daysList
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView

        //get details from date
        val calendar = Calendar.getInstance()
        val date = getItem(position)
        calendar.time = date!!
        val day = calendar.get(Calendar.DATE)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        //get todays calendar
        val todayDate = Date()
        val calendarToday = Calendar.getInstance()
        calendarToday.time = todayDate

        //inflate view if null
        if (view == null) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.date_item, parent, false)
        }

        if (month != calendarToday.get(Calendar.MONTH) || year != calendarToday.get(
                Calendar.YEAR
            )
        ) {
            //grey out
            (view as TextView).setTextColor(Color.parseColor("#E0E0E0"))
        } else {
            (view as TextView).setTextColor(Color.WHITE)
            (view as TextView).gravity = Gravity.CENTER
            view.setBackgroundResource(R.drawable.date_bg_unselected)
        }
        // set text
        (view as TextView).text = calendar[Calendar.DATE].toString()


        return view

    }
}