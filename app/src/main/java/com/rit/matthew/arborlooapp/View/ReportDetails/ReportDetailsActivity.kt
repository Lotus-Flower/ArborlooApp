package com.rit.matthew.arborlooapp.View.ReportDetails

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.databinding.ReportDetailsActivityBinding
import kotlinx.android.synthetic.main.report_details_activity.*

class ReportDetailsActivity : AppCompatActivity(), ReportDetailsContract.View{

    private lateinit var binding: ReportDetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.report_details_activity)

        setSupportActionBar(toolbar_report_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupUI()
        setEventHandlers()
    }


    override fun setupUI() {
        val report = intent.getParcelableExtra("report") as Report
        binding.currentReport = report
    }

    override fun setEventHandlers() {

    }

    override fun displayReportDetails(reportDB: ReportDB) {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}