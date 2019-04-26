package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import com.rit.matthew.arborlooapp.Database.Entities.InfoDB
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReportInfo(var id: Long?, var fullness: Int?, var drainage: Boolean?, var cleanliness: Int?,
                 var covered: Boolean?, var pests: String?, var smell: Int?,
                 var water: Boolean?, var soap: Boolean?, var wipe: Boolean?,
                 var treesInside: String?, var treesOutside: String?, var other: String?) : Parcelable {

    companion object {
        fun fromInfoDB(infoDB: InfoDB): ReportInfo {
            return ReportInfo(infoDB.id, infoDB.fullness, infoDB.drainage, infoDB.cleanliness,
                    infoDB.covered, infoDB.pests, infoDB.smell,
                    infoDB.water, infoDB.soap, infoDB.wipe,
                    infoDB.treesInside, infoDB.treesOutside, infoDB.other)
        }
    }
}