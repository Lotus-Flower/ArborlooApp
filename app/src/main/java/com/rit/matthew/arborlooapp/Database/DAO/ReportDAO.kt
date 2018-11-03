package com.rit.matthew.arborlooapp.Database.DAO

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.TypeConverter.ReportTypeConverter

@Dao
@TypeConverters(ReportTypeConverter::class)
interface ReportDAO{

    @Query("Select * FROM ReportDB")
    fun getReports() : MutableList<ReportDB>

    @Query("Select * FROM ReportDB WHERE id = :reportId LIMIT 1")
    fun getTemperatureData(reportId: Long) : ReportDB

    @Insert(onConflict = REPLACE)
    fun insertReport(reportDB: ReportDB)

    @Delete
    fun deleteReport(reportDB: ReportDB)

    @Query("DELETE FROM ReportDB")
    fun deleteAllReports()

}