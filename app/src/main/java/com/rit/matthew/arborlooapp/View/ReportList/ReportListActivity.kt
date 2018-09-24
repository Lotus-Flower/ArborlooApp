package com.rit.matthew.arborlooapp.View.ReportList

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.R

class ReportListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list_activity)

        val appDB = AppDB.getInstance(this)
        val reportRepository = ReportRepository(appDB = appDB)


        reportRepository.getReports(object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "" + data!!.size)
            }
        })
    }
}
