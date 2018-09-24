package com.rit.matthew.arborlooapp.Database.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ReportDB")
class ReportDB {
    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "temperature")
    var temperature: Int = 0

    @ColumnInfo(name = "moisture")
    var moisture: Int = 0

    @ColumnInfo(name = "oxygen")
    var oxygen: Int = 0

    @ColumnInfo(name = "ph")
    var ph: Int = 0
}