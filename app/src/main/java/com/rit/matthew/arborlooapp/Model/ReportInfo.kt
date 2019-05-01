package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReportInfo(var fullness: Int? = 1,
                 var drainage: Boolean? = false,
                 var cleanliness: Int? = 1,
                 var covered: Boolean? = false,
                 var pests: String? = "",
                 var smell: Int? = 1,
                 var water: Boolean? = false,
                 var soap: Boolean? = false,
                 var wipe: Boolean? = false,
                 var treesInside: String? = "",
                 var treesOutside: String? = "",
                 var other: String? = ""
) : Parcelable {

    companion object {
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