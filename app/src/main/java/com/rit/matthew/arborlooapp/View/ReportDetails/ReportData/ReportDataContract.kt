package com.rit.matthew.arborlooapp.View.ReportDetails.ReportData

interface ReportDataContract {

    interface View{
        fun displayDataList(data: ArrayList<Double>?)
    }

    interface Presenter{
        fun setupDataList(id: Long)
    }

}