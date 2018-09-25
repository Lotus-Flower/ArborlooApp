package com.rit.matthew.arborlooapp.View.ReportList

import com.rit.matthew.arborlooapp.Base.View.BasePresenter
import com.rit.matthew.arborlooapp.Base.View.BaseView
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB

interface ReportListContract {

    interface View : BaseView{

        fun displayReportList(reports: ArrayList<ReportDB>)

    }

    interface Presenter : BasePresenter{

        fun setupReportList()

    }

}