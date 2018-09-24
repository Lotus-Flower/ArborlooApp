package com.rit.matthew.arborlooapp.View.ReportList

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.R

class ReportListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list_activity)

        val appDB = AppDB.getInstance(this)
        val reportRepository = ReportRepository(appDB = appDB)

        val reportDB = ReportDB()

        reportDB.temperature = 1.0
        reportDB.moisture = 2.0
        reportDB.oxygen = 3.0
        reportDB.ph = 4.0

        reportRepository.insertReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "We did it")
                reportRepository.getReports(object : BaseCallback{
                    override fun onSuccess(data: MutableList<*>?) {
                        Log.d("MMMM", "" + data!!.size)
                    }
                })
            }
        })
    }
}
