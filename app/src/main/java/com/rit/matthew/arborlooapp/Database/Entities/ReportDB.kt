package com.rit.matthew.arborlooapp.Database.Entities

import android.arch.persistence.room.*
import com.rit.matthew.arborlooapp.Database.TypeConverter.DataTypeConverter
import com.rit.matthew.arborlooapp.Database.TypeConverter.ReportTypeConverter
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.Model.ReportInfo
import com.rit.matthew.arborlooapp.Model.ReportSurvey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "ReportDB")
@TypeConverters(DataTypeConverter::class, ReportTypeConverter::class)
class ReportDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "temp")
    var temp: ArrayList<ReportData> = ArrayList()

    @ColumnInfo(name = "moist")
    var moist: ArrayList<ReportData> = ArrayList()

    @ColumnInfo(name = "uses")
    var uses: Long? = null

    /*@ColumnInfo(name = "info")
    var info: ReportInfo? = null

    @ColumnInfo(name = "survey")
    var survey: ReportSurvey? = null*/
}