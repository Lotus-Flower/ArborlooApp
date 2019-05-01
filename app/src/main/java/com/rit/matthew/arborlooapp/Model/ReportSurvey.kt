package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import com.rit.matthew.arborlooapp.Database.Entities.SurveyDB
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReportSurvey(var id: Long?, var clean: Int?, var wash: String?, var material: String?,
                   var adult: Int?, var child: Int?, var clinic: String?,
                   var move: Int?, var personMove: String?, var calls: Int?,
                   var trees: Int?, var cover: String?, var coverFreq: String?,
                   var good: String?, var bad: String?, var problems: String?,
                   var broken: String?, var purchase: String?, var cost: Int?) : Parcelable {

    companion object {
        fun fromSurveyDB(surveyDB: SurveyDB): ReportSurvey {
            return ReportSurvey(surveyDB.id, surveyDB.clean, surveyDB.wash, surveyDB.material,
                    surveyDB.adult, surveyDB.child, surveyDB.clinic,
                    surveyDB.move, surveyDB.personMove, surveyDB.calls,
                    surveyDB.trees, surveyDB.cover, surveyDB.coverFreq,
                    surveyDB.good, surveyDB.bad, surveyDB.problems,
                    surveyDB.broken, surveyDB.purchase, surveyDB.cost)
        }

        fun createSurveyHashMap(reportSurvey: ReportSurvey, surveyQuestions: ArrayList<String>): HashMap<String, Any?>{
            val surveyMap = HashMap<String, Any?>()

            surveyMap.put(surveyQuestions.get(0), reportSurvey.clean)
            surveyMap.put(surveyQuestions.get(1), reportSurvey.wash)
            surveyMap.put(surveyQuestions.get(2), reportSurvey.material)
            surveyMap.put(surveyQuestions.get(3), reportSurvey.adult)
            surveyMap.put(surveyQuestions.get(4), reportSurvey.child)
            surveyMap.put(surveyQuestions.get(5), reportSurvey.clinic)
            surveyMap.put(surveyQuestions.get(6), reportSurvey.move)
            surveyMap.put(surveyQuestions.get(7), reportSurvey.personMove)
            surveyMap.put(surveyQuestions.get(8), reportSurvey.calls)
            surveyMap.put(surveyQuestions.get(9), reportSurvey.trees)
            surveyMap.put(surveyQuestions.get(10), reportSurvey.cover)
            surveyMap.put(surveyQuestions.get(11), reportSurvey.coverFreq)
            surveyMap.put(surveyQuestions.get(12), reportSurvey.good)
            surveyMap.put(surveyQuestions.get(13), reportSurvey.bad)
            surveyMap.put(surveyQuestions.get(14), reportSurvey.problems)
            surveyMap.put(surveyQuestions.get(15), reportSurvey.broken)
            surveyMap.put(surveyQuestions.get(16), reportSurvey.purchase)
            surveyMap.put(surveyQuestions.get(17), reportSurvey.cost)

            return surveyMap
        }
    }

}