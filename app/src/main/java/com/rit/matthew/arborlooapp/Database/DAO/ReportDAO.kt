package com.rit.matthew.arborlooapp.Database.DAO

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB

@Dao
interface ReportDAO {

    @Query("Select * FROM ReportDB")
    fun getReports() : MutableList<ReportDB>

    @Insert(onConflict = REPLACE)
    fun insertReport(reportDB: ReportDB)

    @Delete
    fun deleteReport(reportDB: ReportDB)

    @Query("DELETE FROM ReportDB")
    fun deleteAllReports()

}