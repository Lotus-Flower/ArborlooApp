package com.rit.matthew.arborlooapp.View.ReportDetails

import com.rit.matthew.arborlooapp.Base.View.BasePresenter
import com.rit.matthew.arborlooapp.Base.View.BaseView
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB

interface ReportDetailsContract {

    interface View : BaseView{
        fun displayReportDetails(reportDB: ReportDB)
    }

    interface Presenter : BasePresenter{
        fun setupReportDetails(reportId : Int)
    }

}