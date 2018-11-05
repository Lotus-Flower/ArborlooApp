package com.rit.matthew.arborlooapp.Database.Entities

import android.arch.persistence.room.*
import com.rit.matthew.arborlooapp.Database.TypeConverter.DataTypeConverter
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "TemperatureDB", foreignKeys = arrayOf(ForeignKey(entity = ReportDB::class, parentColumns = arrayOf("id"), childColumns = arrayOf("reportId"), onDelete = ForeignKey.CASCADE)))
@TypeConverters(DataTypeConverter::class)
class TemperatureDB {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "reportId")
    var reportId: Long? = null

    @ColumnInfo(name = "data")
    var data: Double? = null

    @ColumnInfo(name = "dateTime")
    var dateTime: OffsetDateTime? = null
}