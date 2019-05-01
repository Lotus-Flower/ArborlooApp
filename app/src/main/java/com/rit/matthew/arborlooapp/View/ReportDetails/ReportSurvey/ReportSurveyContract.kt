package com.rit.matthew.arborlooapp.View.ReportDetails.ReportSurvey

import com.rit.matthew.arborlooapp.Model.Report

interface ReportSurveyContract {

    interface View{

        fun setReport(report: Report)

    }

    interface Presenter{

        fun updateSurvey(report: Report)

        fun destroy()

    }

}