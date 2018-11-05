package com.rit.matthew.arborlooapp.Database.DAO

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.rit.matthew.arborlooapp.Database.Entities.MoistureDB
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Entities.TemperatureDB
import com.rit.matthew.arborlooapp.Database.TypeConverter.DataTypeConverter
import com.rit.matthew.arborlooapp.Database.TypeConverter.ReportTypeConverter

@Dao
@TypeConverters(DataTypeConverter::class)
interface ReportDAO{

    @Query("Select * FROM ReportDB")
    fun getReports() : MutableList<ReportDB>

    @Query("Select * FROM ReportDB WHERE id = :reportId LIMIT 1")
    fun getReportById(reportId: Long) : ReportDB

    @Query("SELECT * FROM TemperatureDB WHERE reportId=:reportId")
    fun getTemperatureData(reportId: Long?): MutableList<TemperatureDB>

    @Query("SELECT * FROM MoistureDB WHERE reportId=:reportId")
    fun getMoistureData(reportId: Long?): MutableList<MoistureDB>

    @Insert(onConflict = REPLACE)
    fun insertReport(reportDB: ReportDB)

    @Insert(onConflict = REPLACE)
    fun insertTemp(temperatureDB: TemperatureDB)

    @Insert(onConflict = REPLACE)
    fun insertMoist(moistureDB: MoistureDB)

    @Delete
    fun deleteReport(reportDB: ReportDB)

    @Delete
    fun deleteTemp(temperatureDB: TemperatureDB)

    @Delete
    fun deleteMoist(moistureDB: MoistureDB)

    @Query("DELETE FROM ReportDB")
    fun deleteAllReports()

}