package com.rit.matthew.arborlooapp.View.ReportDetails.ReportInfo

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report

class ReportInfoPresenter(var view: ReportInfoContract.View?, val reportRepository: ReportRepository) : ReportInfoContract.Presenter{

    private val TAG = "ReportInfoPresenter"

    override fun updateInfo(report: Report) {
        val reportDB = Report.reportToDB(report)

        reportRepository.updateReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                report.id?.let { getReport(it) }
            }

            override fun onFailure() {

            }

        })
    }

    private fun getReport(reportId: Long){
        reportRepository.getReport(reportId, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val reportDB = data?.get(0) as ReportDB

                view?.setReport(Report.reportFromDB(reportDB))
            }

            override fun onFailure() {

            }
        })
    }

    override fun destroy() {
        view = null
    }

}