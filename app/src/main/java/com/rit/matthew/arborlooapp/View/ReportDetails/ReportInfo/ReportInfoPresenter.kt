package com.rit.matthew.arborlooapp.View.ReportDetails.ReportInfo

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.InfoDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.ReportInfo

class ReportInfoPresenter(var view: ReportInfoContract.View, val reportRepository: ReportRepository) : ReportInfoContract.Presenter{

    override fun updateInfo(infoDB: InfoDB) {
        reportRepository.updateInfo(infoDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Updated")
                getInfo(infoDB.reportId)
            }

            override fun onFailure() {
                Log.d("MMMM", "Failure")
            }

        })
    }

    private fun getInfo(reportId: Long?){
        reportRepository.getInfo(reportId, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val info = ReportInfo.fromInfoDB(data!![0] as InfoDB)

                for(surveyData in (data as ArrayList<InfoDB>)){
                    Log.d("MMMM", "entry")
                }

                view.setInfo(info)
            }
            override fun onFailure() {
                Log.d("MMMM", "Failure")
            }

        })
    }

}