package com.rit.matthew.arborlooapp.View.ReportDetails.ReportData

import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository

class ReportDataPresenter(var view: ReportDataFragment, val reportRepository: ReportRepository): ReportDataContract.Presenter {

    override fun setupDataList(id: Long) {

    }

}