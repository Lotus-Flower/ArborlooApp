package com.rit.matthew.arborlooapp.View.ReportDetails.Dashboard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.View.ReportDetails.ReportData.ReportDataFragment
import com.rit.matthew.arborlooapp.View.ReportDetails.ReportGraph.ReportGraphFragment
import kotlinx.android.synthetic.main.dashboard_activity.*

class DashboardActivity : AppCompatActivity() {

    private final val INFO: Int = 0
    private final val SURVEY: Int = 1
    private final val DATA: Int = 2
    private final val GRAPH: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val report: Report = intent.getParcelableExtra("report")
        toolbar.title = report.name

        setupUI()
    }

    private fun setupUI(){
        val adapter: DashboardPagerAdapter = DashboardPagerAdapter(supportFragmentManager)
        pager.adapter = adapter

        tab_layout.setupWithViewPager(pager)

        floating_action_button_data_type.visibility = View.GONE

        setEventHandlers()
    }

    private fun setEventHandlers(){
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position: Int = tab!!.position

                pager.currentItem = position

                when (position) {
                    INFO -> floating_action_button_data_type.visibility = View.GONE
                    SURVEY -> floating_action_button_data_type.visibility = View.GONE
                    DATA -> floating_action_button_data_type.visibility = View.VISIBLE
                    GRAPH -> floating_action_button_data_type.visibility = View.VISIBLE
                }
            }

        })

        floating_action_button_temperature.setOnClickListener {
            val index = pager.getCurrentItem()
            val adapter = pager.adapter as DashboardPagerAdapter

            when(pager.currentItem){
                DATA -> {
                    val tabFragment = adapter.getFragment(index) as ReportDataFragment
                    tabFragment.updateDataTemperature()
                }
                GRAPH -> {
                    val tabFragment = adapter.getFragment(index) as ReportGraphFragment
                    tabFragment.updateDataTemperature()
                }
            }
        }

        floating_action_button_moisture.setOnClickListener {
            val index = pager.getCurrentItem()
            val adapter = pager.adapter as DashboardPagerAdapter

            when(pager.currentItem){
                DATA -> {
                    val tabFragment = adapter.getFragment(index) as ReportDataFragment
                    tabFragment.updateDataMoisture()
                }
                GRAPH -> {
                    val tabFragment = adapter.getFragment(index) as ReportGraphFragment
                    tabFragment.updateDataMoisture()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

}
