package com.rit.matthew.arborlooapp.Database.TypeConverter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportInfo
import com.rit.matthew.arborlooapp.Model.ReportSurvey
import java.util.*
import java.util.Collections.emptyList
import kotlin.collections.ArrayList


class ReportTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun infoFromString(data: String?): ReportInfo {
        val infoType = object : TypeToken<ReportInfo>() {}.type

        return gson.fromJson(data, infoType)
    }

    @TypeConverter
    fun infoToString(data: ReportInfo): String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun surveyFromString(data: String?): ReportSurvey {
        val surveyType = object : TypeToken<ReportSurvey>() {}.type

        return gson.fromJson(data, surveyType)
    }

    @TypeConverter
    fun surveyToString(data: ReportSurvey): String {
        return gson.toJson(data)
    }
}