package com.rit.matthew.arborlooapp.View.ReportDetails.ReportSurvey

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report

class ReportSurveyPresenter(var view: ReportSurveyContract.View?, val reportRepository: ReportRepository) : ReportSurveyContract.Presenter {

    override fun updateSurvey(report: Report) {
        val reportDB = Report.reportToDB(report)

        reportRepository.updateReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", report.survey?.child.toString())
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