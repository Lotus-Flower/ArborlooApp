package com.rit.matthew.arborlooapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReportSurvey(var clean: Int? = 0,
                   var wash: String? = "no",
                   var material: String? = "",
                   var adult: Int? = 0,
                   var child: Int? = 0,
                   var clinic: String? = "",
                   var move: Int? = 0,
                   var personMove: String? = "",
                   var calls: Int? = 0,
                   var trees: Int? = 0,
                   var cover: String? = "",
                   var coverFreq: String? = "less",
                   var good: String? = "",
                   var bad: String? = "",
                   var problems: String? = "",
                   var broken: String? = "",
                   var purchase: String? = "no",
                   var cost: Int? = 500
) : Parcelable {

    companion object {
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