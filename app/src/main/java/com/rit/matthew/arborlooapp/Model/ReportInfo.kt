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

        fun createInfoHashMap(reportInfo: ReportInfo, infoQuestions: ArrayList<String>): HashMap<String, Any?>{
            val infoMap = HashMap<String, Any?>()

            infoMap.put(infoQuestions.get(0), reportInfo.fullness)
            infoMap.put(infoQuestions.get(1), reportInfo.drainage)
            infoMap.put(infoQuestions.get(2), reportInfo.cleanliness)
            infoMap.put(infoQuestions.get(3), reportInfo.covered)
            infoMap.put(infoQuestions.get(4), reportInfo.pests)
            infoMap.put(infoQuestions.get(5), reportInfo.smell)
            infoMap.put(infoQuestions.get(6), reportInfo.water)
            infoMap.put(infoQuestions.get(7), reportInfo.soap)
            infoMap.put(infoQuestions.get(8), reportInfo.wipe)
            infoMap.put(infoQuestions.get(9), reportInfo.treesInside)
            infoMap.put(infoQuestions.get(10), reportInfo.treesOutside)

            return infoMap
        }
    }
}