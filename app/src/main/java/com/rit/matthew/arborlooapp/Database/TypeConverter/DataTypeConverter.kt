package com.rit.matthew.arborlooapp.Database.TypeConverter

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class DataTypeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}