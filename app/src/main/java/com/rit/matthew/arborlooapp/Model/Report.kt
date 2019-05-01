package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Report(var id: Long?, var name: String?, var uses: Long?, var info: ReportInfo?, var survey: ReportSurvey?, var temperatureData: ArrayList<ReportData>, var moistureData: ArrayList<ReportData>) : Parcelable{

    companion object {
        fun reportFromDB(reportDB: ReportDB) : Report{

            val report = Report(reportDB.id, reportDB.name, reportDB.uses, reportDB.info, reportDB.survey, reportDB.temp, reportDB.moist)

            return report
        }

        fun reportToDB(report: Report) : ReportDB{
            val reportDB = ReportDB()

            reportDB.id = report.id
            reportDB.name = report.name
            reportDB.uses = report.uses
            reportDB.info = report.info
            reportDB.survey = report.survey
            reportDB.temp = report.temperatureData
            reportDB.moist = report.moistureData

            return reportDB
        }
    }
}