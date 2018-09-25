package com.rit.matthew.arborlooapp.Database.AppDatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.rit.matthew.arborlooapp.Database.DAO.ReportDAO
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB

@Database(entities = [(ReportDB::class)], version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun reportDAO(): ReportDAO

    companion object {
        private var INSTANCE: AppDB? = null

        fun getInstance(context: Context?): AppDB?{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context!!.applicationContext, AppDB::class.java, "report-database").build()
            }

            return INSTANCE
        }

        fun destroyDatabase(){
            INSTANCE = null
        }
    }
}