package com.rit.matthew.arborlooapp.Database.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ReportDB")
class ReportDB {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "info")
    var info: String? = null

    @ColumnInfo(name = "temperature")
    var temperature: Double? = null

    @ColumnInfo(name = "moisture")
    var moisture: Double? = null

    @ColumnInfo(name = "oxygen")
    var oxygen: Double? = null

    @ColumnInfo(name = "ph")
    var ph: Double? = null
}