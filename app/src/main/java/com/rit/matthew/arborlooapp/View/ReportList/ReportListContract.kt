package com.rit.matthew.arborlooapp.View.ReportList

import com.rit.matthew.arborlooapp.Base.View.BasePresenter
import com.rit.matthew.arborlooapp.Base.View.BaseView
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.Model.ReportInfo
import com.rit.matthew.arborlooapp.Model.ReportSurvey

interface ReportListContract {

    interface View : BaseView{

        fun displayReportList(reports: ArrayList<ReportDB>)

        fun setData(reportDB: ReportDB, info: ReportInfo, survey: ReportSurvey, tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>)

    }

    interface Presenter : BasePresenter{

        fun setupReportList()

        fun getReportData(report: ReportDB)

        fun createReport(reportName: String)

    }

}