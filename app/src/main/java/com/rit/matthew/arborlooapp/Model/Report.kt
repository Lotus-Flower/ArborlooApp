package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Report(var name: String?, var info: String?, var temperature: Double?, var moisture: Double?, var oxygen: Double?, var ph: Double?) : Parcelable{

    companion object {
        fun fromReportDB(reportDB: ReportDB) : Report{

            val report = Report(reportDB.name, reportDB.info, reportDB.temperature, reportDB.moisture, reportDB.oxygen, reportDB.ph)

            return report
        }
    }

}