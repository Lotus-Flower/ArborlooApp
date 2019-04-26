package com.rit.matthew.arborlooapp.Database.DAO

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.rit.matthew.arborlooapp.Database.Entities.*
import com.rit.matthew.arborlooapp.Database.TypeConverter.DataTypeConverter
import com.rit.matthew.arborlooapp.Database.TypeConverter.ReportTypeConverter

@Dao
@TypeConverters(DataTypeConverter::class)
interface ReportDAO{

    @Query("Select * FROM ReportDB")
    fun getReports() : MutableList<ReportDB>

    @Query("Select * FROM ReportDB WHERE id = :reportId LIMIT 1")
    fun getReportById(reportId: Long) : ReportDB

    @Query("SELECT * FROM TemperatureDB WHERE reportId=:reportId ORDER BY datetime(dateTime)")
    fun getTemperatureData(reportId: Long?): MutableList<TemperatureDB>

    @Query("SELECT * FROM MoistureDB WHERE reportId=:reportId ORDER BY datetime(dateTime)")
    fun getMoistureData(reportId: Long?): MutableList<MoistureDB>

    @Query("SELECT * FROM InfoDB WHERE reportId=:reportId ORDER BY id")
    fun getInfo(reportId: Long?): InfoDB

    @Query("SELECT * FROM SurveyDB WHERE reportId=:reportId LIMIT 1")
    fun getSurvey(reportId: Long?): SurveyDB

    @Insert(onConflict = REPLACE)
    fun insertReport(reportDB: ReportDB)

    @Insert(onConflict = REPLACE)
    fun insertTemp(temperatureDB: TemperatureDB)

    @Insert(onConflict = REPLACE)
    fun insertMoist(moistureDB: MoistureDB)

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
    fun deleteTemp(temperatureDB: TemperatureDB)

    @Delete
    fun deleteMoist(moistureDB: MoistureDB)

    @Delete
    fun deleteInfo(infoDB: InfoDB)

    @Delete
    fun deleteSurvey(surveyDB: SurveyDB)

    @Query("DELETE FROM ReportDB")
    fun deleteAllReports()

}