package com.rit.matthew.arborlooapp.View.ReportDetails.ReportSurvey

import com.rit.matthew.arborlooapp.Database.Entities.SurveyDB
import com.rit.matthew.arborlooapp.Model.ReportSurvey

interface ReportSurveyContract {

    interface View{

        fun setSurvey(survey: ReportSurvey)

    }

    interface Presenter{

        fun updateSurvey(surveyDB: SurveyDB)

        fun destroy()

    }

}