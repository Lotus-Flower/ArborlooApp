package com.rit.matthew.arborlooapp.View.ReportList

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.MoistureDB
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Entities.TemperatureDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.ReportData

class ReportListPresenter(var view: ReportListActivity?, val reportRepository: ReportRepository) : ReportListContract.Presenter{

    private var gotTempData: Boolean = false
    private var gotMoistData: Boolean = false

    private var temperatureData: ArrayList<ReportData> = ArrayList()
    private var moistureData: ArrayList<ReportData> = ArrayList()

    override fun setupReportList() {
        reportRepository.getReports(object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val reports = ArrayList(data) as ArrayList<ReportDB>
                view?.displayReportList(reports)
            }

        })
    }

    override fun getReportData(reportId: Long?) {

        reportRepository.getTemperatureData(reportId, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                temperatureData.clear()
                for(tempEntry in (data as ArrayList<TemperatureDB>)){
                    temperatureData.add(ReportData.fromTempDB(tempEntry))
                }
                gotTempData = true
                checkData()
            }
        })

        reportRepository.getMoistureData(reportId, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                moistureData.clear()
                for(moistEntry in (data as ArrayList<MoistureDB>)){
                    moistureData.add(ReportData.fromMoistDB(moistEntry))
                }
                gotMoistData = true
                checkData()
            }
        })
    }

    fun checkData(){
        if(gotTempData && gotMoistData){
            gotTempData = false
            gotMoistData = false
            view?.setData(temperatureData, moistureData)
        }
    }

    override fun destroy() {
        view = null
    }

}