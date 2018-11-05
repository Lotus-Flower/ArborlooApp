package com.rit.matthew.arborlooapp.View.ReportList

import com.rit.matthew.arborlooapp.Base.View.BasePresenter
import com.rit.matthew.arborlooapp.Base.View.BaseView
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Model.ReportData

interface ReportListContract {

    interface View : BaseView{

        fun displayReportList(reports: ArrayList<ReportDB>)

        fun setData(tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>)

    }

    interface Presenter : BasePresenter{

        fun setupReportList()

        fun getReportData(reportId: Long?)

    }

}