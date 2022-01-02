package com.tekkr.flypigeonsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.calendar_months_item.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarPickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_months_item)

        setCalendar()
    }

    private fun setCalendar() {

        val cells = ArrayList<Date>()
        val currentDate = Calendar.getInstance()
        val calendar = currentDate.clone() as Calendar

        //determine cell for current month beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1

        //move calendar backwards to beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

        //caluculate max days in a month
        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //fill cells
        while (cells.size <= maxDays) {
            cells.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        gv_days.adapter = CalendarAdapter(this, cells)

    }
}