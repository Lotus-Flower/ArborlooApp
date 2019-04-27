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
    }

}