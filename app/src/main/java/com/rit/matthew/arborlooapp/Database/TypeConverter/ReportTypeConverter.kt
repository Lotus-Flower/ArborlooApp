package com.rit.matthew.arborlooapp.Database.TypeConverter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rit.matthew.arborlooapp.Model.Report
import java.util.*
import java.util.Collections.emptyList
import kotlin.collections.ArrayList


class ReportTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToDoubleList(data: String?): ArrayList<Double> {
        val listType = object : TypeToken<ArrayList<Double>>() {}.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun doubleListToString(objectList: ArrayList<Double>): String {
        return gson.toJson(objectList)
    }
}