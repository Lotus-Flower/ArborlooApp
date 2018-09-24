package com.rit.matthew.arborlooapp.Database.DAO

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB

@Dao
interface ReportDAO {

    @Query("Select * FROM ReportDB")
    fun getReports() : MutableList<ReportDB>

}