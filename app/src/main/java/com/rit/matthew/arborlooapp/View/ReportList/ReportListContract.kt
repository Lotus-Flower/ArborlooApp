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

        fun setSerialData(tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>, uses: Long)

        fun setData(reportDB: ReportDB)

    }

    interface Presenter : BasePresenter{

        fun setupReportList()

        fun getReportData(reportId: Long)

        fun createReport(reportName: String, tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>, uses: Long)

        fun parseSerialData(data: String)

    }

}