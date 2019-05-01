package com.rit.matthew.arborlooapp.Database.DAO

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.rit.matthew.arborlooapp.Database.Entities.*
import com.rit.matthew.arborlooapp.Database.TypeConverter.DataTypeConverter
import com.rit.matthew.arborlooapp.Database.TypeConverter.ReportTypeConverter
import io.reactivex.Observable
import io.reactivex.Single

@Dao
@TypeConverters(DataTypeConverter::class)
interface ReportDAO{

    @Query("Select * FROM ReportDB")
    fun getReports() : MutableList<ReportDB>

    @Query("Select * FROM ReportDB WHERE id = :reportId")
    fun getReportById(reportId: Long) : Single<ReportDB>

    @Insert(onConflict = REPLACE)
    fun insertReport(reportDB: ReportDB)

    @Update
    fun updateReport(reportDB: ReportDB)

    @Delete
    fun deleteReport(reportDB: ReportDB)

    @Query("DELETE FROM ReportDB")
    fun deleteAllReports()

}