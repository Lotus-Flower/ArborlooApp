package com.rit.matthew.arborlooapp.Database.TypeConverter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.rit.matthew.arborlooapp.Model.ReportData
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import com.google.gson.reflect.TypeToken



class DataTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun toDataList(value: String?): ArrayList<ReportData> {
        val arrayListType = object : TypeToken<ArrayList<ReportData>>(){

        }.type
        return gson.fromJson(value, arrayListType)
    }

    @TypeConverter
    fun fromDataList(value: ArrayList<ReportData>): String? {
        return gson.toJson(value)
    }
}