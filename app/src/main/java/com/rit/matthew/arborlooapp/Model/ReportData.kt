package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.math.pow

@Parcelize
class ReportData(var data: Double?, var dateTime: Long?) : Parcelable {

    companion object {
        fun calculateTemp(value: Double): Double{
            return -46.85 + 175.72 * (value / (2.0).pow(16))
        }

        fun calculateMoisture(value: Double): Double{
            return -6.0 + 125 * (value / (2.0).pow(16))
        }
    }

}