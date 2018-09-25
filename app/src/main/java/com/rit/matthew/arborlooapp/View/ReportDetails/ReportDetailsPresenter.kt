package com.rit.matthew.arborlooapp.View.ReportDetails

import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository

class ReportDetailsPresenter(var view: ReportDetailsActivity?, val reportRepository: ReportRepository) : ReportDetailsContract.Presenter {

    override fun setupReportDetails(reportId : Int) {

    }

    override fun destroy() {

    }
}