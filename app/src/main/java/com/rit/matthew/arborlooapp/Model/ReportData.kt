package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import com.rit.matthew.arborlooapp.Database.Entities.MoistureDB
import com.rit.matthew.arborlooapp.Database.Entities.TemperatureDB
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
class ReportData(var data: Double?, var dateTime: OffsetDateTime?) : Parcelable {
    companion object {
        fun fromMoistDB(moistureDB: MoistureDB) : ReportData{

            val reportData = ReportData(moistureDB.data, moistureDB.dateTime)

            return reportData
        }
        fun fromTempDB(temperatureDB: TemperatureDB) : ReportData{

            val reportData = ReportData(temperatureDB.data, temperatureDB.dateTime)

            return reportData
        }
    }
}