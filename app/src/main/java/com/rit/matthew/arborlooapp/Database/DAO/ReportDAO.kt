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

    @Query("SELECT * FROM InfoDB WHERE reportId=:reportId ORDER BY id")
    fun getInfo(reportId: Long?): InfoDB

    @Query("SELECT * FROM SurveyDB WHERE reportId=:reportId LIMIT 1")
    fun getSurvey(reportId: Long?): SurveyDB

    @Insert(onConflict = REPLACE)
    fun insertReport(reportDB: ReportDB)

    @Insert(onConflict = REPLACE)
    fun insertInfo(infoDB: InfoDB)

    @Insert(onConflict = REPLACE)
    fun insertSurvey(surveyDB: SurveyDB)

    @Update
    fun updateInfo(infoDB: InfoDB)

    @Update
    fun updateSurvey(surveyDB: SurveyDB)

    @Delete
    fun deleteReport(reportDB: ReportDB)

    @Delete
    fun deleteInfo(infoDB: InfoDB)

    @Delete
    fun deleteSurvey(surveyDB: SurveyDB)

    @Query("DELETE FROM ReportDB")
    fun deleteAllReports()

}