package com.rit.matthew.arborlooapp.View.ReportDetails.ReportSurvey

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.SurveyDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.ReportSurvey

class ReportSurveyPresenter(var view: ReportSurveyContract.View?, val reportRepository: ReportRepository) : ReportSurveyContract.Presenter {

    override fun updateSurvey(surveyDB: SurveyDB) {
        reportRepository.updateSurvey(surveyDB, object : BaseCallback {
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Updated")
                getSurvey(surveyDB.reportId)
            }

            override fun onFailure() {
                Log.d("MMMM", "Failure")
            }

        })
    }

    private fun getSurvey(reportId: Long?){
        reportRepository.getSurvey(reportId, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val survey = ReportSurvey.fromSurveyDB(data!![0] as SurveyDB)

                view?.setSurvey(survey)
            }
            override fun onFailure() {
                Log.d("MMMM", "Failure")
            }

        })
    }

    override fun destroy() {
        view = null
    }

}