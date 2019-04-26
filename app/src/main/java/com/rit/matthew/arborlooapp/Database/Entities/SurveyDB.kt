package com.rit.matthew.arborlooapp.Database.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "SurveyDB", foreignKeys = arrayOf(ForeignKey(entity = ReportDB::class, parentColumns = arrayOf("id"), childColumns = arrayOf("reportId"), onDelete = ForeignKey.CASCADE)))
class SurveyDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "reportId")
    var reportId: Long? = null

    @ColumnInfo(name = "clean")
    var clean: Int? = null

    @ColumnInfo(name = "wash")
    var wash: String? = null

    @ColumnInfo(name = "material")
    var material: String? = null

    @ColumnInfo(name = "adult")
    var adult: Int? = null

    @ColumnInfo(name = "child")
    var child: Int? = null

    @ColumnInfo(name = "clinic")
    var clinic: String? = null

    @ColumnInfo(name = "move")
    var move: Int? = null

    @ColumnInfo(name = "personMove")
    var personMove: String? = null

    @ColumnInfo(name = "calls")
    var calls: Int? = null

    @ColumnInfo(name = "trees")
    var trees: Int? = null

    @ColumnInfo(name = "cover")
    var cover: String? = null

    @ColumnInfo(name = "coverFreq")
    var coverFreq: String? = null

    @ColumnInfo(name = "good")
    var good: String? = null

    @ColumnInfo(name = "bad")
    var bad: String? = null

    @ColumnInfo(name = "problems")
    var problems: String? = null

    @ColumnInfo(name = "broken")
    var broken: String? = null

    @ColumnInfo(name = "purchase")
    var purchase: String? = null

    @ColumnInfo(name = "cost")
    var cost: Int? = null
}