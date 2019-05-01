package com.rit.matthew.arborlooapp.View.ReportDetails.ReportInfo

import com.rit.matthew.arborlooapp.Model.Report

interface ReportInfoContract {

    interface View{

        fun setReport(report: Report)

    }

    interface Presenter{

        fun updateInfo(report: Report)

        fun destroy()

    }

}