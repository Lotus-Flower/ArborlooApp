package com.rit.matthew.arborlooapp.View.ReportDetails.ReportGraph

import android.util.Log
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class AxisFormatter : IAxisValueFormatter{

    //Puts epoch data into lovely date time format
    private val dateFormat = SimpleDateFormat("MM/dd", Locale.US)

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val epochValue = (value).toLong() * 1000
        return dateFormat.format(Date(epochValue))
    }

}