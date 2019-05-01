package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Report(var id: Long?, var name: String?, var info: ReportInfo?, var survey: ReportSurvey?, var temperatureData: ArrayList<ReportData>, var moistureData: ArrayList<ReportData>) : Parcelable{

    companion object {
        fun constructReportfromDB(reportDB: ReportDB, info: ReportInfo?, survey: ReportSurvey?, temperatureData: ArrayList<ReportData>, moistureData: ArrayList<ReportData>) : Report{

            val report = Report(reportDB.id, reportDB.name, info, survey, temperatureData, moistureData)

            return report
        }
    }
}