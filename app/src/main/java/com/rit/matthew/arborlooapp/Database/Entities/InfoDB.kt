package com.rit.matthew.arborlooapp.Database.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "InfoDB", foreignKeys = arrayOf(ForeignKey(entity = ReportDB::class, parentColumns = arrayOf("id"), childColumns = arrayOf("reportId"), onDelete = ForeignKey.CASCADE)))
class InfoDB {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "reportId")
    var reportId: Long? = null

    @ColumnInfo(name = "fullness")
    var fullness: Int? = null

    @ColumnInfo(name = "drainage")
    var drainage: Boolean? = null

    @ColumnInfo(name = "cleanliness")
    var cleanliness: Int? = null

    @ColumnInfo(name = "covered")
    var covered: Boolean? = null

    @ColumnInfo(name = "pests")
    var pests: String? = null

    @ColumnInfo(name = "smell")
    var smell: Int? = null

    @ColumnInfo(name = "water")
    var water: Boolean? = null

    @ColumnInfo(name = "soap")
    var soap: Boolean? = null

    @ColumnInfo(name = "wipe")
    var wipe: Boolean? = null

    @ColumnInfo(name = "treesInside")
    var treesInside: String? = null

    @ColumnInfo(name = "treesOutside")
    var treesOutside: String? = null

    @ColumnInfo(name = "other")
    var other: String? = null

}