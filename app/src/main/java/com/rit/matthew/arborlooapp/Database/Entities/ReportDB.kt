package com.rit.matthew.arborlooapp.Database.Entities

import android.arch.persistence.room.*
import com.rit.matthew.arborlooapp.Database.TypeConverter.ReportTypeConverter
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "ReportDB")
@TypeConverters(ReportTypeConverter::class)
class ReportDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "name")
    var name: String? = null

}