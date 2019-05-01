package com.rit.matthew.arborlooapp.View.ReportList

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.*
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.Model.ReportInfo
import com.rit.matthew.arborlooapp.Model.ReportSurvey
import org.threeten.bp.OffsetDateTime
import kotlin.math.pow

class ReportListPresenter(var view: ReportListActivity?, val reportRepository: ReportRepository) : ReportListContract.Presenter{

    private lateinit var reportDB: ReportDB
    private var survey: ReportSurvey? = null
    private var info: ReportInfo? = null
    private var temperatureData: ArrayList<ReportData> = ArrayList()
    private var moistureData: ArrayList<ReportData> = ArrayList()
    private var uses: Long? = 0

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
        this.temperatureData = report.temp
        this.moistureData = report.moist
        this.uses = report.uses

        getInfoData()
    }

    private fun getInfoData(){

        reportRepository.getInfo(reportDB.id, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                info = ReportInfo.fromInfoDB(data!![0] as InfoDB)

                getSurveyData()
            }
            override fun onFailure() {
                getSurveyData()
            }

        })
    }

    private fun getSurveyData(){

        reportRepository.getSurvey(reportDB.id, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                survey = ReportSurvey.fromSurveyDB(data!![0] as SurveyDB)

                setData()
            }
            override fun onFailure() {
                setData()
            }

        })
    }

    fun setData(){
        view?.setData(reportDB, info, survey, temperatureData, moistureData, uses)
    }

    override fun createReport(reportName: String, tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>, uses: Long) {
        val reportDB = ReportDB()

        reportDB.name = reportName
        reportDB.temp = tempData
        reportDB.moist = moistData
        reportDB.uses = uses

        reportRepository.insertReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Report created")
                setupReportList()
            }
            override fun onFailure() {

            }
        })
    }

    override fun parseSerialData(data: String) {

        var tempArray: ArrayList<String> = ArrayList()
        var moistArray: ArrayList<String> = ArrayList()
        var usageArray: ArrayList<String> = ArrayList()

        val dataArray: Array<String> = data.split(";").toTypedArray()

        for(index in dataArray.indices){
            if(index == 0){
                tempArray = dataArray[0].split(",").toTypedArray().toCollection(ArrayList())
            }else if(index == 1){
                moistArray = dataArray[1].split(",").toTypedArray().toCollection(ArrayList())
            }else if(index == 2){
                usageArray = dataArray[2].split(",").toTypedArray().toCollection(ArrayList())
            }
        }

        for(use in usageArray){
            Log.d("MMMM", "use: " + use)
        }


        if(usageArray.size > 1){
            usageArray.removeAt(1)
        }
        else{
            usageArray.clear()
            usageArray.add("0")
        }

        if(tempArray.size > 1){
            tempArray.removeAt(tempArray.size - 1)
        }

        if(moistArray.size > 1){
            moistArray.removeAt(moistArray.size - 1)
        }

        val tempData = ArrayList<ReportData>()
        val moistData = ArrayList<ReportData>()

        for(tempIndex in tempArray.indices){
            tempData.add(ReportData(calculateTemp(tempArray.get(tempIndex).toDouble()), OffsetDateTime.now().toEpochSecond() - ((tempArray.size - 1) - tempIndex) * 43200))
        }

        for(moistIndex in moistArray.indices){
            moistData.add(ReportData(calculateMoisture(moistArray.get(moistIndex).toDouble()), OffsetDateTime.now().toEpochSecond() - ((tempArray.size - 1) - moistIndex) * 43200))
        }

        view?.setSerialData(tempData, moistData, usageArray.get(0).toLong())
    }

    private fun calculateTemp(value: Double): Double{
        return -46.85 + 175.72 * (value / (2.0).pow(16))
    }

    private fun calculateMoisture(value: Double): Double{
        return -6.0 + 125 * (value / (2.0).pow(16))
    }

    override fun destroy() {
        view = null
    }

}