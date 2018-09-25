package com.rit.matthew.arborlooapp.View.ReportList

import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository

class ReportListPresenter(var view: ReportListActivity?, val reportRepository: ReportRepository) : ReportListContract.Presenter{
    override fun setupReportList() {
        reportRepository.getReports(object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val reports = ArrayList(data) as ArrayList<ReportDB>
                view?.displayReportList(reports)
            }

        })
    }

    override fun destroy() {
        view = null
    }

}