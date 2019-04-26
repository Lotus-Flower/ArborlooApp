package com.rit.matthew.arborlooapp.View.ReportList

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.*
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.Model.ReportInfo
import com.rit.matthew.arborlooapp.Model.ReportSurvey

class ReportListPresenter(var view: ReportListActivity?, val reportRepository: ReportRepository) : ReportListContract.Presenter{

    private var gotTempData: Boolean = false
    private var gotMoistData: Boolean = false
    private var gotSurvey: Boolean = false

    private lateinit var reportDB: ReportDB
    private lateinit var survey: ReportSurvey
    private lateinit var info: ReportInfo
    private var temperatureData: ArrayList<ReportData> = ArrayList()
    private var moistureData: ArrayList<ReportData> = ArrayList()

    override fun setupReportList() {
        reportRepository.getReports(object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val reports = ArrayList(data) as ArrayList<ReportDB>
                view?.displayReportList(reports)
            }
            override fun onFailure() {

            }

        })
    }

    override fun getReportData(report: ReportDB) {
        this.reportDB = report

        getInfoData()
    }

    private fun getInfoData(){
        reportRepository.getInfo(reportDB.id, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                info = ReportInfo.fromInfoDB(data!![0] as InfoDB)

                for(surveyData in (data as ArrayList<InfoDB>)){
                    Log.d("MMMM", "entry")
                }

                getSurveyData()
            }
            override fun onFailure() {

            }

        })
    }

    private fun getSurveyData(){
        reportRepository.getSurvey(reportDB.id, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                survey = ReportSurvey.fromSurveyDB(data!![0] as SurveyDB)

                for(surveyData in (data as ArrayList<SurveyDB>)){
                    Log.d("MMMM", "entry")
                }

                getTemperatureData()
            }
            override fun onFailure() {

            }

        })
    }

    private fun getTemperatureData(){
        reportRepository.getTemperatureData(reportDB.id, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                temperatureData.clear()
                for(tempEntry in (data as ArrayList<TemperatureDB>)){
                    temperatureData.add(ReportData.fromTempDB(tempEntry))
                }
                getMoistureData()
            }
            override fun onFailure() {

            }
        })
    }

    private fun getMoistureData(){
        reportRepository.getMoistureData(reportDB.id, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                moistureData.clear()
                for(moistEntry in (data as ArrayList<MoistureDB>)){
                    moistureData.add(ReportData.fromMoistDB(moistEntry))
                }
                setData()
            }
            override fun onFailure() {

            }
        })
    }

    fun setData(){
        view?.setData(reportDB, info, survey, temperatureData, moistureData)
    }

    override fun createReport(reportName: String) {
        val reportDB = ReportDB()

        reportDB.name = reportName

        reportRepository.insertReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Report created")
                setupReportList()
            }
            override fun onFailure() {

            }
        })
    }

    override fun destroy() {
        view = null
    }

}