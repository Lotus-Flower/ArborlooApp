package com.rit.matthew.arborlooapp.View.ReportDetails.ReportData

import com.rit.matthew.arborlooapp.Model.ReportData

interface ReportDataContract {

    interface View{
        fun displayDataList(data: ArrayList<ReportData>?)
    }

    interface Presenter{
        fun setupDataList(id: Long)
    }

}