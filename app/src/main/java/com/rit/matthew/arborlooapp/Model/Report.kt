package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Report(var name: String?, var info: String?) : Parcelable{

    companion object {
        fun fromReportDB(reportDB: ReportDB) : Report{

            val report = Report(reportDB.name, reportDB.info)

            return report
        }
    }
}